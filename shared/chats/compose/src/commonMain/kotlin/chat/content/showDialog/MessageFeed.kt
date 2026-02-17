package chat.content.showDialog

import CommonPaddings
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    val density = LocalDensity.current

    val topPadding = LocalFeedTopPadding.current
    val bottomPadding = LocalFeedBottomPadding.current

    // СЛОЖНАЯ СИСТЕМА ОТСТУПОВ СДЕЛАНА, ЧТОБЫ РИСОВАТЬ UI ПОД LIQUID GLASS КЛАВИАТУРОЙ!!
    val keyboardOffset = CommonPaddings.calculateKeyboardOffset()

    val animatedPadding by animateDpAsState(
        targetValue = keyboardOffset,
        animationSpec = if (keyboardOffset > 0.dp) snap() else tween(300)
    )

    var previousRawOffset by remember { mutableStateOf(0.dp) }

    LaunchedEffect(keyboardOffset) {
        val diff = keyboardOffset - previousRawOffset
        if (diff != 0.dp) {
            val diffPx = with(density) { diff.toPx() }

            lazyState.scrollBy(diffPx)
        }
        previousRawOffset = keyboardOffset
    }



    LazyColumn(
        modifier = modifier.widthIn(max = 800.dp).fillMaxSize(),
        state = lazyState,
        contentPadding = PaddingValues(
            top = 16.dp + topPadding,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp + bottomPadding + maxOf(keyboardOffset, animatedPadding)
        ),
        verticalArrangement = Arrangement.Bottom
    ) {
        items(items = messages, key = { it.id }) { message ->
            Column(Modifier.animateItem()) {
                if (message.isFromMe) {
                    UserMessage(message = message)
                    Spacer(Modifier.height(12.dp))
                } else {
                    AIMessage(message = message)
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun AIMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    Box(modifier.fillMaxWidth(.9f)) {
        Text(
            text = message.text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.animateContentSize()
        )
    }
}


@Composable
private fun UserMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth(.8f)
        ) {
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp))
                    .background(backgroundGradient)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
