package chat

import chat.ChatState.LoadingError
import chats.mvi.ChatListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.AsyncDispatcher
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.withStateOrThrow
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce

private typealias Ctx = PipelineContext<ChatState, ChatIntent, Nothing>

class ChatContainer(
    private val chatConfig: ChatListItem?,
    coroutineScope: CoroutineScope
) : Container<ChatState, ChatIntent, Nothing> {

    override val store = store(initial = ChatState.Loading, scope = coroutineScope) {
        recover {
            updateState { LoadingError(it) }
            null
        }
        init {
            loadChat()
        }

        recover {
            withStateOrThrow<ChatState.ShowDialog, _> {
                updateState { this@withStateOrThrow.copy(error = it) }
            }
            null
        }
        reduce { intent ->
            when (intent) {
                ChatIntent.SentMessage -> TODO()
                is ChatIntent.TypedMessage -> TODO()
            }
        }
    }

    private fun Ctx.loadChat() = launch(AsyncDispatcher) {
        // TODO
    }
}