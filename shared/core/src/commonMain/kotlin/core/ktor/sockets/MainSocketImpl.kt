package core.ktor.sockets

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import network.MainSocket
import presentation.AsyncDispatcher
import utils.api.Event
import utils.api.ServerException
import utils.api.SocketState
import utils.api.WSFrame
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainSocketImpl(
    private val client: HttpClient
) : MainSocket {
    override val events: MutableSharedFlow<Event> = MutableSharedFlow()
    override val state: MutableStateFlow<SocketState> = MutableStateFlow(SocketState.Idle)

    private val scope = CoroutineScope(AsyncDispatcher + SupervisorJob())

    private var sessionJob: Job? = null
    private var session: DefaultClientWebSocketSession? = null

    private val requestsMutex = Mutex()
    private val pendingRequests = mutableMapOf<String, CompletableDeferred<Event>>()

    @OptIn(ExperimentalSerializationApi::class)
    override fun connect(url: String) {
        sessionJob?.cancel()
        sessionJob = scope.launch {
            while (isActive) {
                state.value = SocketState.Connecting
                try {
                    client.webSocket(url) {
                        state.value = SocketState.Connected
                        session = this
                        for (frame in incoming) {
                            if (frame is Frame.Binary) {
                                val wsFrame = ProtoBuf.decodeFromByteArray<WSFrame>(frame.data)
                                processFrame(wsFrame)
                            }
                        }
                    }
                } catch (e: Exception) {
                    if (isActive) {
                        state.value = SocketState.Disconnected(e)
                    }
                } finally {
                    if (state.value !is SocketState.Disconnected) {
                        state.value = SocketState.Disconnected(null)
                    }
                    cleanup()
                }

                if (isActive) delay(5000) // перед рекконектом делэй

            }
        }
    }

    override fun disconnect() {
        sessionJob?.cancel()
    }

    private suspend fun processFrame(wsFrame: WSFrame) {
        val requestId = wsFrame.id
        val event = wsFrame.event

        val deferred = requestsMutex.withLock { pendingRequests.remove(requestId) }

        if (requestId != null && deferred != null) {
            if (event is Event.Error) {
                deferred.completeExceptionally(ServerException(event.message))
            } else {
                deferred.complete(event)
            }
        } else {
            events.emit(event)
        }
    }

    @OptIn(ExperimentalSerializationApi::class, ExperimentalUuidApi::class)
    override suspend fun send(event: Event): Event {
        val requestId = Uuid.random().toString()
        val deferred = CompletableDeferred<Event>()

        requestsMutex.withLock {
            pendingRequests[requestId] = deferred
        }

        return try {
            val frame = WSFrame(id = requestId, event = event)
            val bytes = ProtoBuf.encodeToByteArray(WSFrame.serializer(), frame)

            val currentSession = session ?: throw IllegalStateException("Socket not connected")
            currentSession.send(Frame.Binary(true, bytes))

            withTimeout(15000) { // TODO
                deferred.await()
            }
        } catch (e: Exception) {
            requestsMutex.withLock { pendingRequests.remove(requestId) }
            throw e
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fire(event: Event) {
        val frame = WSFrame(id = null, event = event)
        val bytes = ProtoBuf.encodeToByteArray(WSFrame.serializer(), frame)
        session?.send(Frame.Binary(true, bytes)) ?: throw IllegalStateException("Not connected")
    }

    private suspend fun cleanup() {
        session = null
        requestsMutex.withLock {
            val error =
                (state.value as? SocketState.Disconnected)?.cause ?: Exception("Socket closed")
            pendingRequests.values.forEach { it.completeExceptionally(error) }
            pendingRequests.clear()
        }
    }
}