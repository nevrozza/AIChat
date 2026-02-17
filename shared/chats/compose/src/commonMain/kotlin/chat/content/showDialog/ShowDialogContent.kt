package chat.content.showDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chats.entity.ChatMessage

@Composable
fun ShowDialogContent(
    messages: List<ChatMessage>,
    isAnswering: Boolean,
    isSending: Boolean,
    lastError: Exception?
) {
    MessageFeed(
        modifier = Modifier,
        messages = messages,
        isAnswering = isAnswering,
        isSending = isSending,
        lastError = lastError
    )
}