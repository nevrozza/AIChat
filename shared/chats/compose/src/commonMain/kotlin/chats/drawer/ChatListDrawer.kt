package chats.drawer

import CommonPaddings
import CommonPaddings.calculateBottomPadding
import CommonPaddings.calculateTopPadding
import aichat.shared.chats.compose.generated.resources.Res
import aichat.shared.chats.compose.generated.resources.edit_square
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chats.ChatsComponent
import chats.ChatsComponent.Config
import chats.entity.ChatListItem
import chats.mvi.ChatsIntent
import chats.mvi.ChatsState
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.painterResource
import pro.respawn.flowmvi.essenty.compose.subscribe

@Composable
internal fun ChatListDrawer(
    component: ChatsComponent,
) {
    val containerShapeDp = 40.dp

    val containerShape = RoundedCornerShape(containerShapeDp)

    val contentShapeDp = 30.dp
    val contentShape = RoundedCornerShape(
        topStart = contentShapeDp,
        topEnd = contentShapeDp,
        bottomStart = containerShapeDp,
        bottomEnd = containerShapeDp
    )

    val containerColor = MaterialTheme.colorScheme.surface
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainer

    Column(
        Modifier.padding(
            start = CommonPaddings.horizontalContentPadding,
            bottom = calculateBottomPadding(),
            top = calculateTopPadding()
        ).clip(containerShape)
            .drawBehind {
                drawRect(backgroundColor)
            }
    ) {
        DrawerTopRow(component)
        Box(
            Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = contentShape
                )

                .clip(contentShape)
                .drawBehind {
                    drawRect(containerColor)
                }
        ) {
            ChatListDrawerContent(component)
        }
    }
}

@Composable
private fun ChatListDrawerContent(component: ChatsComponent) {
    val state by component.subscribe(component)

    when (state) {
        is ChatsState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // for centering error message
                TextButton(
                    onClick = {},
                    modifier = Modifier.alpha(0f),
                    enabled = false
                ) { Text("Попробовать ещё раз") }

                Text(
                    (state as ChatsState.Error).e?.message ?: "Unknown error",
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = { TODO() }) {
                    Text("Попробовать ещё раз")
                }
            }
        }

        ChatsState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ChatsState.OK -> {
            val verticalPadding = 15.dp
            val buttonModifier = Modifier.fillMaxWidth()

            val stack by component.stack.subscribeAsState()
            val curId = (stack.active.configuration as? Config.Chat)?.id


            LazyColumn(Modifier.padding(horizontal = 15.dp)) {
                item("topPadding") {
                    Spacer(Modifier.height(verticalPadding))
                }

                item("NewChat") {
                    DrawerListButton(
                        modifier = buttonModifier,
                        isSelected = curId == null,
                        text = "Новый чат",
                        icon = painterResource(Res.drawable.edit_square)
                    ) {
                        component.intent(
                            ChatsIntent.SelectedChat(null)
                        )
                    }
                }

                items(items = (state as ChatsState.OK).chats + ChatListItem(
                    id = "123",
                    title = "Привет! Подскажи, пожалуйста, как"
                ), key = { it.id }) { chatInfo ->
                    DrawerListButton(
                        modifier = buttonModifier,
                        isSelected = (chatInfo.id == curId),
                        text = chatInfo.title,
                        icon = null
                    ) { component.intent(ChatsIntent.SelectedChat(chatInfo.id)) }
                }


                item("bottomPadding") {
                    Spacer(Modifier.height(verticalPadding))
                }
            }
        }
    }
}