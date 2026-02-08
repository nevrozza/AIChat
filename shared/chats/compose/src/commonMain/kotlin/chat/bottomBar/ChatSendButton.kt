package chat.bottomBar

import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.send
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatSendButton(
    modifier: Modifier,
    isTextEmpty: Boolean,
    isBlockable: Boolean,
    onClick: () -> Unit
) {
    val colors = ChatBottomBarDefaults.SendButtonColors

    val animatedColor by animateColorAsState(
        if (!isBlockable && isTextEmpty) colors.disabled
        else if (isBlockable) colors.enabled_block
        else colors.enabled_send
    )

    SurfaceButton(modifier, onClick = onClick, enabled = !isTextEmpty) {
        CompositionLocalProvider(
            LocalContentColor provides animatedColor
        ) {
            Icon(
                painter = painterResource(Res.drawable.send),
                contentDescription = "Send",
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}