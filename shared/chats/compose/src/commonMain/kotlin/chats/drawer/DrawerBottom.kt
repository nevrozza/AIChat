package chats.drawer

import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.power
import aichat.shared.chats.compose.generated.resources.power_off
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chat.bottomBar.ChatBottomBarDefaults
import org.jetbrains.compose.resources.painterResource
import utils.api.SocketState

@Composable
internal fun DrawerBottom(
    url: String,
    socketState: SocketState,
    onURLChange: (String) -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    val textStyle = LocalTextStyle.current
    val colors = ChatBottomBarDefaults.TextFieldColors

    val contentShapeDp = 30.dp
    val contentShape = RoundedCornerShape(contentShapeDp)

    val containerColor = MaterialTheme.colorScheme.surface

    Column(
        Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 10.dp).padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth().border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = contentShape
            )

                .clip(contentShape)
                .drawBehind {
                    drawRect(containerColor)
                }
        ) {
            BasicTextField(
                value = url,
                onValueChange = onURLChange,
                textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(colors.cursor),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier.weight(1f, true),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        Text(
                            text = "Введите url сервера",
                            style = textStyle.copy(fontWeight = FontWeight.Medium),
                            color = colors.placeholder,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.matchParentSize()
                                .alpha(if (url.isEmpty()) 1f else 0f)
                        )

                        innerTextField()
                    }
                },
                modifier = Modifier.weight(1f, true).padding(start = 20.dp, end = 10.dp)
            )

            val isNotConnected =
                socketState is SocketState.Idle || socketState is SocketState.Disconnected

            Button(
                onClick = if (isNotConnected) onConnectClick else onDisconnectClick,
                modifier = Modifier.fillMaxHeight()
            ) {
                AnimatedContent(
                    if (isNotConnected) Res.drawable.power
                    else Res.drawable.power_off
                ) { resource ->
                    Icon(
                        painter = painterResource(resource),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(Modifier.height(5.dp))

        var showErrorLog by remember { mutableStateOf(true) }

        Text(
            when (socketState) {
                SocketState.Connected -> "Connected"
                SocketState.Connecting -> "Connecting"
                is SocketState.Disconnected -> "Disconnected"
                SocketState.Idle -> "Idle"
            },
            modifier = Modifier.clickable(enabled = socketState is SocketState.Disconnected) {
                showErrorLog = !showErrorLog
            }
        )
        AnimatedVisibility(
            showErrorLog && socketState is SocketState.Disconnected
        ) {
            Box(Modifier.animateContentSize()) {
                if (socketState is SocketState.Disconnected) {
                    Text(socketState.cause?.message ?: "unknown error")
                }
            }
        }

    }
}