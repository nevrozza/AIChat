package utils.api

sealed class SocketState {
    object Idle : SocketState()
    object Connecting : SocketState()
    object Connected : SocketState()
    data class Disconnected(val cause: Throwable? = null) : SocketState()
}