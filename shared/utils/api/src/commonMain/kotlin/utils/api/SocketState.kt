package utils.api

sealed interface SocketState {
    object Idle : SocketState
    object Connecting : SocketState
    object Connected : SocketState
    data class Disconnected(val cause: Throwable? = null) : SocketState
}