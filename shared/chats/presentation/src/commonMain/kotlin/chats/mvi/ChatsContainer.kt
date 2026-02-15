package chats.mvi


import chats.mvi.ChatsAction.*
import chats.mvi.ChatsAction.SelectChat
import chats.mvi.ChatsState.ChatsContent
import chats.usecases.ChatListUseCases
import chats.usecases.ConnectToChatsWSUseCases
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateImmediate
import pro.respawn.flowmvi.plugins.enableLogging
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce

private typealias Ctx = PipelineContext<ChatsState, ChatsIntent, ChatsAction>

class ChatsContainer(
    private val chatListUseCases: ChatListUseCases,
    private val connectToChatsWSUseCases: ConnectToChatsWSUseCases
) : Container<ChatsState, ChatsIntent, ChatsAction> {

    override val store =
        store(initial = ChatsState(url = "ws://0.0.0.0:8080", content = ChatsContent.Loading)) {
            configure {
                name = "Chats"
                debuggable = true
            }
            enableLogging()
            recover {
                updateState { this.copy(content = ChatsContent.Error(it)) }
                null
            }
            init {
                loadChatsList()
            }
            reduce { intent ->
                when (intent) {
                    is ChatsIntent.SetDrawerOpened -> {
                        action(SetDrawerOpened(intent.isOpened))
                    }

                    is ChatsIntent.SelectedChat -> action(SelectChat(intent.id))
                    ChatsIntent.ClickedConnect -> withState {
                        connectToChatsWSUseCases.connect(url = this.url)
                    }

                    ChatsIntent.ClickedDisconnect -> connectToChatsWSUseCases.disconnect()
                    is ChatsIntent.ChangeServerUrl -> updateStateImmediate {
                        this.copy(url = intent.url)
                    }

                }
            }
        }

    private fun Ctx.loadChatsList() = launch {
        updateState {
            this.copy(content = ChatsContent.OK(chats = listOf())) // TODO
        }
    }
}