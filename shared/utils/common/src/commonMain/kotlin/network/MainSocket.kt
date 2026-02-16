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

    suspend fun send(event: Event): Event
    suspend fun fire(event: Event)
}