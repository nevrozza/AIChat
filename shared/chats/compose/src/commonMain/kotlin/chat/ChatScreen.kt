package chat

import CommonPaddings
import CommonPaddings.calculateBottomPadding
import CommonPaddings.calculateTopPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import chat.ChatState.MessageFeed
import chat.bottomBar.ChatBottomBar
import chat.content.LoadingContent
import chat.content.LoadingErrorContent
import chat.content.NewChatContent
import chat.content.showDialog.ShowDialogContent
import flowMVI.TypeCrossfade
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.essenty.compose.subscribe

@Composable
internal fun ChatScreen(
    component: ChatComponent
) {

    val container = component.container
    val state by container.store.subscribe(component)

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = CommonPaddings.horizontalContentPadding),
        bottomBar = {
            StateChatBottomBar(
                state,
                onTextChange = { container.intent(ChatIntent.TypedMessage(it)) },
                onSendClick = { container.intent(ChatIntent.SentMessage) }
            )
        },
        topBar = {
            ChatTopBar(
                modifier = Modifier.fillMaxWidth().padding(top = calculateTopPadding()),
                title = state.chatTitle,
                onDrawerClick = component.onDrawerClick,
                onNewChatClick = { },
                isNewChatButtonVisible = state.messageFeed is MessageFeed.ShowDialog
            )
        }
    ) {
        TypeCrossfade(
            state.messageFeed,
            modifier = Modifier
        ) {
            when (this) {
                MessageFeed.Loading -> LoadingContent()
                is MessageFeed.LoadingError -> {
                    LoadingErrorContent(error = this.error) { }
                }

                MessageFeed.NewChat -> NewChatContent()
                is MessageFeed.ShowDialog -> ShowDialogContent(
                    messages = this.messages,
                    isAnswering = this.isAnswering,
                    isSending = this.isSending,
                    lastError = this.error
                )
            }
        }
    }
}

@Composable
private fun StateChatBottomBar(
    state: ChatState,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
//    val animatedAlpha by animateFloatAsState(if (state.messageFeed is ChatState.MessageFeed.ShowDialog) 1f else .5f)
    Box(
        Modifier.fillMaxWidth().padding(bottom = calculateBottomPadding()).imePadding(),
        contentAlignment = Alignment.Center
    ) {
        ChatBottomBar(
            modifier = Modifier.alpha(1f),
            text = state.inputText,
            isAnswering = (state.messageFeed as? MessageFeed.ShowDialog)?.isAnswering ?: false,
            onTextChange = onTextChange,
            onSendClick = onSendClick
        )
    }
}