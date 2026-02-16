package chats

import chat.RealChatComponent
import chats.ChatsComponent.Child
import chats.ChatsComponent.Child.ChatChild
import chats.ChatsComponent.Config
import chats.ChatsComponent.Config.Chat
import chats.entity.ChatListItem
import chats.mvi.ChatsAction
import chats.mvi.ChatsAction.SelectChat
import chats.mvi.ChatsContainer
import chats.mvi.ChatsIntent
import chats.mvi.ChatsState
import chats.mvi.ChatsState.ChatsContent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import pro.respawn.flowmvi.api.DelicateStoreApi
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.state
import pro.respawn.flowmvi.essenty.dsl.retainedStore
import pro.respawn.flowmvi.essenty.dsl.subscribe
import utils.api.SocketState

@OptIn(DelicateStoreApi::class)
class RealChatsComponent(
    componentCtx: ComponentContext,
    container: () -> ChatsContainer,
) :
    ChatsComponent, KoinComponent, ComponentContext by componentCtx,
    Store<ChatsState, ChatsIntent, ChatsAction> by componentCtx.retainedStore(
        factory = container
    ) {
    override val socketState: StateFlow<SocketState> = get()

    override val uiEvents: MutableSharedFlow<ChatsComponent.UIEvent> =
        MutableSharedFlow(extraBufferCapacity = 64)

    override val nav: StackNavigation<Config> = StackNavigation()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Chat(null, message = null),
        childFactory = { config, childCtx ->

            val cfg = config as? Chat
            if (cfg != null) {
                @OptIn(DelicateStoreApi::class)
                val chatConfig: ChatListItem? = cfg.id?.let {
                    if (cfg.message == null) {
                        (this.state.content as? ChatsContent.OK)?.chats?.firstOrNull { it.id == config.id }
                    } else {
                        ChatListItem(id = cfg.id, title = cfg.message)
                    }
                }
                val child = ChatChild(
                    RealChatComponent(
                        childCtx,
                        chatConfig = chatConfig,
                        onChatCreate = { id, message ->
                            navigateToChat(chatId = id, newSendMessage = message)
                        },
                        initialMessage = cfg.message,
                        onDrawerClick = { intent(ChatsIntent.SetDrawerOpened(true)) },
                        navigateToEmptyNewChat = { navigateToChat(null, null) }
                    )
                )
                // child.component.container.intent() TODO: update messages?

                child
            } else {
                throw IllegalStateException()
            }
        },
        handleBackButton = true
    )

    private fun navigateToChat(chatId: String?, newSendMessage: String?) {
        nav.pushToFront(Chat(chatId, message = newSendMessage))
    }

    override val stack: Value<ChildStack<Config, Child>>
        get() = _stack

    init {
        subscribe {
            actions.collect { action ->
                when (action) {
                    is SelectChat -> navigateToChat(action.id, newSendMessage = null)
                    is ChatsAction.SetDrawerOpened ->
                        uiEvents.emit(ChatsComponent.UIEvent.SetDrawerOpened(action.isOpened))

                }
            }
        }
    }


}