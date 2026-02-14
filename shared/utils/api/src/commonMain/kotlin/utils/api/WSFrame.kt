package utils.api

import kotlinx.serialization.Serializable

@Serializable
data class WSFrame(
    val id: String? = null,
    val event: Event
)