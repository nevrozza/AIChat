package chat.bottomBar

import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.send
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatSendButton(
    modifier: Modifier,
    isBlockable: Boolean,
    onClick: () -> Unit
) {
    SurfaceButton(modifier, onClick = onClick) {
        Icon(
            painter = painterResource(Res.drawable.send),
            contentDescription = "Send",
            modifier = Modifier.padding(10.dp),
        )
    }
}