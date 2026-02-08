package chat

import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.drawer_open
import aichat.shared.chats.compose.generated.resources.send
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import chats.drawer.LocalChatDrawerState
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ChatTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onDrawerClick: () -> Unit,
    onNewChatClick: () -> Unit,
    isNewChatButtonVisible: Boolean
) {
    val drawerState = LocalChatDrawerState.current

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.minimumInteractiveComponentSize()) {
            AnimatedVisibility(
                drawerState.targetValue == DrawerValue.Closed
            ) {
                IconButton(onClick = onDrawerClick) {
                    Icon(
                        painter = painterResource(Res.drawable.drawer_open),
                        contentDescription = "OpenDrawer"
                    )
                }
            }
        }

        Text(
            title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, false)
        )


        Column(Modifier.minimumInteractiveComponentSize()) {
            AnimatedVisibility(
                isNewChatButtonVisible
            ) {
                IconButton(onClick = onNewChatClick) {
                    Icon(
                        painter = painterResource(Res.drawable.send),
                        contentDescription = "NewChat"
                    )
                }
            }
        }
    }

}
