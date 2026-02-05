package chats

import chat.RealChatComponent
import chats.ChatsComponent.Child
import chats.ChatsComponent.Child.ChatChild
import chats.ChatsComponent.Config
import chats.mvi.ChatListItem
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
import pro.respawn.flowmvi.api.DelicateStoreApi
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.state
import pro.respawn.flowmvi.essenty.dsl.retainedStore
import pro.respawn.flowmvi.essenty.dsl.subscribe
import presentation.componentCoroutineScope

class RealChatsComponent(
    componentCtx: ComponentContext,
    container: () -> ChatsContainer,
) :
    ChatsComponent, ComponentContext by componentCtx,
    Store<ChatsState, ChatsIntent, ChatsAction> by componentCtx.retainedStore(
        scope = componentCtx.componentCoroutineScope,
        factory = container
    ) {
    override val nav: StackNavigation<Config> = StackNavigation()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Chat(null),
        childFactory = { config, childCtx ->
            @OptIn(DelicateStoreApi::class)
            val chatConfig: ChatListItem? = (config as? Config.Chat)?.id?.let {
                (this.state.content as? ChatsState.Content.OK)?.chats?.firstOrNull { it.id == config.id }
            }
            ChatChild(
                RealChatComponent(childCtx, chatConfig = chatConfig)
            )
        },
        handleBackButton = true
    )

    override val stack: Value<ChildStack<Config, Child>>
        get() = _stack

    init {
        subscribe {
            actions.collect { action ->
                when (action) {
                    is SelectChat -> nav.pushToFront(Config.Chat(action.id))
                }
            }
        }
    }


}