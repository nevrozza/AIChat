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
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import pro.respawn.flowmvi.api.DelicateStoreApi
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.state
import pro.respawn.flowmvi.essenty.dsl.retainedStore
import pro.respawn.flowmvi.essenty.dsl.subscribe

@OptIn(DelicateStoreApi::class)
class RealChatsComponent(
    componentCtx: ComponentContext,
    container: () -> ChatsContainer,
) :
    ChatsComponent, ComponentContext by componentCtx,
    Store<ChatsState, ChatsIntent, ChatsAction> by componentCtx.retainedStore(
        factory = container
    ) {

    override val uiEvents: MutableSharedFlow<ChatsComponent.UIEvent> =
        MutableSharedFlow(extraBufferCapacity = 64)

    override val nav: StackNavigation<Config> = StackNavigation()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Chat(null),
        childFactory = { config, childCtx ->
            @OptIn(DelicateStoreApi::class)
            val chatConfig: ChatListItem? = (config as? Chat)?.id?.let {
                (this.state as? ChatsState.OK)?.chats?.firstOrNull { it.id == config.id }
            }
            ChatChild(
                RealChatComponent(
                    childCtx,
                    chatConfig = chatConfig
                ) {
                    intent(ChatsIntent.SetDrawerOpened(true))
                }
            )
        },
        handleBackButton = true
    )

    override val stack: Value<ChildStack<Config, Child>>
        get() = _stack

    init {
        subscribe {
            println("smth")
            actions.collect { action ->
                when (action) {
                    is SelectChat -> nav.pushToFront(Chat(action.id))
                    is ChatsAction.SetDrawerOpened ->
                        uiEvents.emit(ChatsComponent.UIEvent.SetDrawerOpened(action.isOpened))

                }
            }
        }
    }

}