package chat.bottomBar

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ChatBottomBar(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    onSendClick: () -> Unit,
    onTextChange: (String) -> Unit
) {
    Row(modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
        ChatTextField(modifier = Modifier.fillMaxHeight(), text = text, onTextChange = onTextChange)
        ChatSendButton(
            modifier = Modifier.fillMaxHeight().aspectRatio(1f),
            isLoading = isLoading,
            onClick = onSendClick
        )
    }
}