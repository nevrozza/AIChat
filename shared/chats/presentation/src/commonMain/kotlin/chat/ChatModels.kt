package chat

import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

data class ChatMessage(
    val text: String,
    val isFromMe: Boolean
)


// immutability?
data class ChatState(
    val inputText: String = "",
    val messageFeed: MessageFeed,
) : MVIState {
    sealed interface MessageFeed : MVIState {
        data object NewChat : MessageFeed
        data object Loading : MessageFeed
        data class LoadingError(val error: Exception) : MessageFeed
        data class ShowDialog(
            val messages: List<ChatMessage> = emptyList(),
            val isSending: Boolean = false,
            val isAnswering: Boolean = false,
            val error: Exception? = null
        ) : MessageFeed
    }
}

sealed interface ChatIntent : MVIIntent {
    data class TypedMessage(val text: String) : ChatIntent
    data object SentMessage : ChatIntent
}