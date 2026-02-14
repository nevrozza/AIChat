package chat

import chat.ChatState.MessageFeed
import chats.entity.ChatListItem
import chats.usecases.ChatUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.AsyncDispatcher
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateImmediate
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce

private typealias Ctx = PipelineContext<ChatState, ChatIntent, Nothing>

class ChatContainer(
    private val chatConfig: ChatListItem?,
    private val chatUseCase: ChatUseCases,
    coroutineScope: CoroutineScope
) : Container<ChatState, ChatIntent, Nothing> {

    override val store = store(
        initial = ChatState(
            chatTitle = chatConfig?.title ?: "Новый чат",
            messageFeed = if (chatConfig == null) MessageFeed.NewChat else MessageFeed.Loading
        ),
        scope = coroutineScope
    ) {
        if (chatConfig != null) {
            recover {
                updateState { copy(messageFeed = MessageFeed.LoadingError(it)) }
                null
            }
            init {
                loadChat()
            }
        }
        recover {
            updateState {
                if (messageFeed is MessageFeed.ShowDialog) {
                    copy(messageFeed = messageFeed.copy(error = it))
                } else {
                    println("meow")
                    this
                }
            }
            null
        }
        reduce { intent ->
            when (intent) {
                ChatIntent.SentMessage -> TODO()
                is ChatIntent.TypedMessage -> updateStateImmediate {
                    copy(inputText = intent.text)
                }
            }

        }

    }

    private fun Ctx.loadChat() = launch(AsyncDispatcher) {
        // TODO
    }
}