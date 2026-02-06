package chat

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import chat.bottomBar.ChatBottomBar
import pro.respawn.flowmvi.compose.dsl.subscribe
import pro.respawn.flowmvi.dsl.intent

@Composable
internal fun ChatScreen(
    component: ChatComponent
) {
    val container = component.container
    val state by container.store.subscribe()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ContainerChatBottomBar(
                state,
                onTextChange = { container.intent(ChatIntent.TypedMessage(it)) },
                onSendClick = { container.intent(ChatIntent.SentMessage) }
            )
        }
    ) {
        Text("meow")
    }
}

@Composable
private fun ContainerChatBottomBar(
    state: ChatState,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
//    val animatedAlpha by animateFloatAsState(if (state.messageFeed is ChatState.MessageFeed.ShowDialog) 1f else .5f)
    ChatBottomBar(
        modifier = Modifier.alpha(1f),
        text = state.inputText,
        isAnswering = state.isAnswering,
        onTextChange = onTextChange,
        onSendClick = onSendClick
    )
}