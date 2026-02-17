package chat

import chat.ChatState.MessageFeed
import chats.entity.ChatListItem
import chats.entity.ChatMessage
import chats.usecases.ChatUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presentation.AsyncDispatcher
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateImmediate
import pro.respawn.flowmvi.plugins.enableLogging
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed
import kotlin.collections.listOf
import kotlin.time.Duration.Companion.seconds

private typealias Ctx = PipelineContext<ChatState, ChatIntent, Nothing>

class ChatContainer(
    private val chatConfig: ChatListItem?,
    private val initialMessage: String?,
    private val chatUseCases: ChatUseCases,
    private val onChatCreate: (String, String) -> Unit,
    coroutineScope: CoroutineScope
) : Container<ChatState, ChatIntent, Nothing> {

    override val store = store(
        initial = ChatState(
            chatTitle = chatConfig?.title ?: "Новый чат",
            messageFeed = if (initialMessage != null) MessageFeed.ShowDialog() else if (chatConfig == null) MessageFeed.NewChat else MessageFeed.Loading
        ),
        scope = coroutineScope
    ) {
        configure {
            name = "ChatDialog"
            debuggable = true
        }
        enableLogging()

        if (chatConfig != null) {
            recover {
                updateState { copy(messageFeed = MessageFeed.LoadingError(it)) }
                null
            }
            init {
                loadChat()
            }
        }
        if (initialMessage != null) {
            init {
                sendMessage(initialMessage)
            }
        }

        recover {
            updateState {
                if (messageFeed is MessageFeed.ShowDialog) {
                    copy(messageFeed = messageFeed.copy(error = it))
                } else {
                    this
                }
            }
            null
        }
        if (chatConfig != null) {
            whileSubscribed(stopDelay = 0.seconds) {
                chatUseCases.subscribeOnChat(chatConfig.id)
                chatUseCases.observeChat(chatConfig.id).collect { newMessages ->
                    updateState {
                        copy(messageFeed = MessageFeed.ShowDialog(newMessages))
                    }
                }
            }
        }

        reduce { intent ->
            when (intent) {
                ChatIntent.SentMessage -> withState { sendMessage(this.inputText) }
                is ChatIntent.TypedMessage -> updateStateImmediate {
                    copy(inputText = intent.text)
                }
            }

        }

    }

    private fun Ctx.sendMessage(sentText: String) = launch(AsyncDispatcher) {
        withState {
            val currentMessages = ((this.messageFeed as? MessageFeed.ShowDialog)?.messages
                ?: listOf<ChatMessage>())
            val itWasNew =
                this.messageFeed is MessageFeed.NewChat

            updateState {
                this.copy(
                    inputText = "",
                    messageFeed = MessageFeed.ShowDialog(
                        messages = currentMessages + ChatMessage(sentText, true),
                        isSending = true
                    )
                )
            }

            if (itWasNew) {
                val id = chatUseCases.createChat(sentText)
                launch(Dispatchers.Main) { // triggers decompose navigation
                    onChatCreate(
                        id,
                        sentText
                    )
                }
                updateState {
                    this.copy(inputText = "", messageFeed = MessageFeed.NewChat)
                }
                return@withState
            } else {
                chatUseCases.sendMessage(chatId = chatConfig!!.id, sentText)
            }
        }
    }

    private fun Ctx.loadChat() = launch(AsyncDispatcher) {
        // TODO
    }
}