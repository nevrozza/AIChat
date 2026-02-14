package utils.api

import kotlinx.serialization.Serializable


interface Event {

    interface ServerEvent : Event

    interface ClientEvent : Event


    @Serializable
    data object AllGucci : Event

    @Serializable
    data class Error(val message: String) : Event
}
