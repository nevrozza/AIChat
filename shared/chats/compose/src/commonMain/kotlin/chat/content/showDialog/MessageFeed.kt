package chat.content.showDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chat.ChatMessage

@Composable
internal fun MessageFeed(
    modifier: Modifier,
    messages: List<ChatMessage>,
    isAnswering: Boolean,
    isSending: Boolean,
    lastError: Exception?
) {

}