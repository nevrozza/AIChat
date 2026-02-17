package chat

import CommonPaddings
import CommonPaddings.calculateTopPadding
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withCompositionLocals
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import chat.ChatState.MessageFeed
import chat.bottomBar.ChatBottomBar
import chat.content.LoadingContent
import chat.content.LoadingErrorContent
import chat.content.NewChatContent
import chat.content.showDialog.LocalFeedBottomPadding
import chat.content.showDialog.LocalFeedTopPadding
import chat.content.showDialog.ShowDialogContent
import com.arkivanov.essenty.lifecycle.Lifecycle
import flowMVI.TypeCrossfade
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.essenty.compose.subscribe

@Composable
internal fun ChatScreen(
    component: ChatComponent
) {

    val container = component.container
    val state by container.store.subscribe(
        lifecycleOwner = component,
        lifecycleState = Lifecycle.State.RESUMED
    )
    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                StateChatBottomBar(
                    state,
                    onTextChange = { container.intent(ChatIntent.TypedMessage(it)) },
                    onSendClick = { container.intent(ChatIntent.SentMessage) }
                )
            },
            topBar = {
                ChatTopBar(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = calculateTopPadding())
                        .padding(horizontal = CommonPaddings.horizontalTopBarPadding),
                    title = state.chatTitle,
                    onDrawerClick = component.onDrawerClick,
                    onNewChatClick = component.navigateToEmptyNewChat,
                    isNewChatButtonVisible = state.messageFeed !is MessageFeed.NewChat
                )
            }
        ) { paddings ->
            val topPadding = paddings.calculateTopPadding()
            val bottomPadding = paddings.calculateBottomPadding()
            Box() {
                TypeCrossfade(
                    state.messageFeed,
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = CommonPaddings.messagesPadding),
                    animationSpec = tween(durationMillis = 300)
                ) {
                    when (this) {
                        MessageFeed.Loading -> LoadingContent()
                        is MessageFeed.LoadingError -> {
                            LoadingErrorContent(
                                error = this.error,
                                onTryAgainClick = component::restartState
                            )
                        }

                        MessageFeed.NewChat -> NewChatContent()

                        is MessageFeed.ShowDialog -> withCompositionLocals(
                            LocalFeedTopPadding provides topPadding,
                            LocalFeedBottomPadding provides bottomPadding
                        ) {
                            ShowDialogContent(
                                messages = this.messages,
                                isAnswering = this.isAnswering,
                                isSending = this.isSending,
                                lastError = this.error
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
                        .height(topPadding * 1.5f)
                        .background(
                            Brush.verticalGradient(
                                0f to MaterialTheme.colorScheme.surface,
                                .3f to MaterialTheme.colorScheme.surface.copy(alpha = .9f),
                                .5f to MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                                1f to Color.Transparent
                            )
                        )
                )

                Box(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .height(bottomPadding)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
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
    val keyboardOffset = CommonPaddings.calculateKeyboardOffset()
    val animatedAlpha by animateFloatAsState(if (state.messageFeed is MessageFeed.ShowDialog || state.messageFeed is MessageFeed.NewChat) 1f else .5f)
    Box(
        Modifier.fillMaxWidth().offset { IntOffset(0, -keyboardOffset.roundToPx()) }
            .padding(horizontal = CommonPaddings.horizontalBottomBarPadding)
            .padding(bottom = CommonPaddings.calculateBottomPadding()),
        contentAlignment = Alignment.Center
    ) {
        ChatBottomBar(
            modifier = Modifier.widthIn(max = 800.dp).alpha(animatedAlpha),
            text = state.inputText,
            isAnswering = (state.messageFeed as? MessageFeed.ShowDialog)?.isAnswering ?: false,
            onTextChange = onTextChange,
            onSendClick = onSendClick
        )
    }
}