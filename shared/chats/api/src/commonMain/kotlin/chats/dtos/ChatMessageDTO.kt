package chats.dtos

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class ChatMessageDTO(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = Clock.System.now().epochSeconds,
    val chatId: String,
    val id: String,
    val isFull: Boolean
)