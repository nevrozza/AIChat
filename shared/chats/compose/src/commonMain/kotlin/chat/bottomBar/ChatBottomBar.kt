package chat.bottomBar

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ChatBottomBar(
    modifier: Modifier = Modifier,
    text: String,
    isAnswering: Boolean,
    onSendClick: () -> Unit,
    onTextChange: (String) -> Unit
) {
    val minHeight = 50.dp
    Row(
        modifier
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.Bottom
    ) {
        ChatTextField(
            modifier = Modifier
                .heightIn(min = minHeight)
                .widthIn(min = 350.dp)
                .weight(1f, false),
            text = text,
            onTextChange = onTextChange
        )
        Spacer(Modifier.width(10.dp))
        ChatSendButton(
            modifier = Modifier.height(minHeight).aspectRatio(1f),
            isBlockable = isAnswering,
            onClick = onSendClick,
            isTextEmpty = text.isEmpty()
        )
    }
}