package core.ktor.sockets

import kotlinx.coroutines.flow.SharedFlow
import utils.api.Event

interface MainSocket {
    val events: SharedFlow<Event>

    fun connect(url: String)

    suspend fun send(event: Event)
}