package chat

import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

data class ChatMessage(
    val text: String,
    val isFromMe: Boolean
)


// immutability?
sealed interface ChatState : MVIState {
    data object Loading : ChatState
    data class LoadingError(val error: Exception?) : ChatState
    data class ShowDialog(
        val messages: List<ChatMessage> = emptyList(),
        val isSending: Boolean = false,
        val error: Exception? = null
    ) : ChatState
}

sealed interface ChatIntent : MVIIntent {
    data class TypedMessage(val text: String) : ChatIntent
    data object SentMessage : ChatIntent
}