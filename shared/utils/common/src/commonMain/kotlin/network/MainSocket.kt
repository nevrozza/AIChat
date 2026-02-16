package network

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import utils.api.Event
import utils.api.SocketState

interface MainSocket {
    val events: SharedFlow<Event>

    val state: StateFlow<SocketState>

    fun connect(url: String)
    fun disconnect()

    suspend fun sendRaw(event: Event): Event
    suspend fun fire(event: Event)
}

suspend inline fun <reified T : Event> MainSocket.send(event: Event): T {
    val result = sendRaw(event)
    return result as? T
        ?: throw IllegalStateException("Expected ${T::class}, but got ${result::class}")
}