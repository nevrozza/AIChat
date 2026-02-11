package core.ktor.sockets

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import presentation.AsyncDispatcher
import utils.api.Event
import utils.api.WSFrame

class MainSocketImpl(
    private val client: HttpClient
) : MainSocket {
    override val events: MutableSharedFlow<Event> = MutableSharedFlow()

    private val scope = CoroutineScope(AsyncDispatcher + SupervisorJob())

    private var sessionJob: Job? = null
    private var session: DefaultClientWebSocketSession? = null

    @OptIn(ExperimentalSerializationApi::class)
    override fun connect(url: String) {
        sessionJob?.cancel()
        sessionJob = scope.launch {
            try {
                client.webSocket(url) {
                    println("socket connect")
                    session = this
                    events.emit(Event.Connected)

                    for (frame in incoming) {
                        if (frame is Frame.Binary) {
                            val wsFrame = ProtoBuf.decodeFromByteArray<WSFrame>(frame.data)

                            println("socket event: $wsFrame")
                            events.emit(wsFrame.event)
                        }
                    }

                }
            } catch (e: Exception) {
                println("socket error: ${e.message}") // hihi

                events.emit(Event.Error(e.message ?: "Unknown error"))
            } finally {
                println("socket disconnect")
                events.emit(Event.Disconnected)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun send(event: Event) {
        val frame = WSFrame(event = event)
        val bytes = ProtoBuf.encodeToByteArray(WSFrame.serializer(), frame)

        val currentSession = session ?: throw IllegalStateException("socket not connected")
        currentSession.send(Frame.Binary(true, bytes))
    }
}