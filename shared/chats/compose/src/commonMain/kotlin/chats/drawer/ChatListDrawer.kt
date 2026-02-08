package chats.drawer

import CommonPaddings
import CommonPaddings.calculateBottomPadding
import CommonPaddings.calculateTopPadding
import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.drawer_close
import aichat.shared.chats.compose.generated.resources.light_mode
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import chats.ChatsComponent
import chats.mvi.ChatsIntent
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ChatListDrawer(
    component: ChatsComponent,
) {
    val shape = RoundedCornerShape(40.dp)
    val containerColor = MaterialTheme.colorScheme.surface
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainer

    Column(
        Modifier.padding(
            start = CommonPaddings.horizontalContentPadding,
            bottom = calculateBottomPadding(),
            top = calculateTopPadding()
        ).clip(shape)
            .drawBehind {
                drawRect(backgroundColor)
            }
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
        Box(
            Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = shape
                )

                .clip(shape)
                .drawBehind {
                    drawRect(containerColor)
                }
        ) {

        }
    }
}