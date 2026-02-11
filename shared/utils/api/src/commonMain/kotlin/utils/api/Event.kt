package utils.api

import kotlinx.serialization.Serializable

@Serializable
sealed interface Event {
    @Serializable
    sealed interface ServerEvent : Event

    @Serializable
    sealed interface ClientEvent : Event


    // Common events
    @Serializable
    data object Connected : Event
    @Serializable
    data object Disconnected : Event
    @Serializable
    data class Error(val message: String) : Event
}
