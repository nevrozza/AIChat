package chat.bottomBar

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import chat.locals.LocalParentHeight

@Composable
internal fun ChatBottomBar(
    modifier: Modifier = Modifier,
    text: String,
    isAnswering: Boolean,
    onSendClick: () -> Unit,
    onTextChange: (String) -> Unit
) {
    val minHeight = 50.dp

    val parentHeight = LocalParentHeight.current

    val density = LocalDensity.current

    val keyboardHeight = WindowInsets.ime.getBottom(density)
    val keyboardHeightDp = with(density) { keyboardHeight.toDp() }

    val maxHeight by remember(parentHeight, keyboardHeightDp) {
        derivedStateOf {
            ((parentHeight - keyboardHeightDp) * 0.4f).coerceAtLeast(minHeight)
        }
    }

    Row(
        modifier
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.Bottom
    ) {
        ChatTextField(
            modifier = Modifier
                .heightIn(
                    min = minHeight,
                    max = maxHeight
                )
                .widthIn(min = 350.dp)
                .weight(1f, false),
            text = text,
            onTextChange = onTextChange,
            onSendClick = onSendClick
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