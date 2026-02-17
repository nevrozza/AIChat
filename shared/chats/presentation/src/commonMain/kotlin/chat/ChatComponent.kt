package chat

import chats.entity.ChatListItem
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import presentation.componentCoroutineScope

interface ChatComponent : ComponentContext, LifecycleOwner {
    val container: ChatContainer

    val onDrawerClick: () -> Unit

    val navigateToEmptyNewChat: () -> Unit
}

class RealChatComponent(
    componentCtx: ComponentContext,
    chatConfig: ChatListItem?,
    initialMessage: String?,
    onChatCreate: (String, String) -> Unit,
    override val onDrawerClick: () -> Unit,
    override val navigateToEmptyNewChat: () -> Unit
) : ChatComponent, KoinComponent, ComponentContext by componentCtx {
    override val container = ChatContainer(
        chatConfig = chatConfig,
        chatUseCases = get(),
        onChatCreate = onChatCreate,
        initialMessage = initialMessage,
        coroutineScope = componentCoroutineScope
    )
}
