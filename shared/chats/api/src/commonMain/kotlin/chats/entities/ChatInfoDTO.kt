package chats.entities

import kotlinx.serialization.Serializable

@Serializable
data class ChatInfoDTO(
    val id: String,
    val title: String
)
