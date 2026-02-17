package chat.content.showDialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chats.entity.ChatMessage

@Composable
internal fun MessageFeed(
    modifier: Modifier,
    messages: List<ChatMessage>,
    isAnswering: Boolean,
    isSending: Boolean,
    lastError: Exception?
) {
    Text(lastError?.message.toString())
}