package chat

import chats.entity.ChatListItem
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import presentation.componentCoroutineScope

interface ChatComponent: ComponentContext {
    val container: ChatContainer

    val onDrawerClick: () -> Unit
}

class RealChatComponent(
    componentCtx: ComponentContext,
    chatConfig: ChatListItem?,
    override val onDrawerClick: () -> Unit
) : ChatComponent, KoinComponent, ComponentContext by componentCtx {
    override val container = ChatContainer(chatConfig, chatUseCase = get(), componentCoroutineScope)
}
