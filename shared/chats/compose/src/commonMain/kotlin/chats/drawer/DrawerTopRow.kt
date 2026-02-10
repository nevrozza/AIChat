package chats.drawer

import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.drawer_close
import aichat.shared.chats.compose.generated.resources.light_mode
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chats.ChatsComponent
import chats.mvi.ChatsIntent
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun DrawerTopRow(
    component: ChatsComponent
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { }
        ) {
            Icon(
                painter = painterResource(Res.drawable.light_mode), // TODO
                contentDescription = "ChangeTheme",
            )
        }
        Text("AI Chat")
        IconButton(
            onClick = { component.intent(ChatsIntent.SetDrawerOpened(false)) }
        ) {
            Icon(
                painter = painterResource(Res.drawable.drawer_close),
                contentDescription = "CloseDrawer",
            )
        }
    }
}