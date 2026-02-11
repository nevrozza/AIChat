package utils.api

import kotlinx.serialization.Serializable

@Serializable
data class WSFrame(
    val event: Event
)
