package chat.content.showDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chats.entity.ChatMessage

@Composable
internal fun MessageFeed(
    modifier: Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    messages: List<ChatMessage>,
    isAnswering: Boolean,
    isSending: Boolean,
    lastError: Exception?
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = messages, key = { it.id }) { message ->
            if (message.isFromMe) {
                UserMessage(message = message)
            } else {
                AIMessage(message = message)
            }
        }

        // Индикатор того, что AI печатает
        if (isAnswering && messages.lastOrNull()?.isFromMe == true) {
            item(key = "typing_indicator") {
                Text(
                    text = "AI печатает...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AIMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}


@Composable
private fun UserMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        }
    }
}