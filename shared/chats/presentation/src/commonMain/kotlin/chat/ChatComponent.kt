package chat

import chats.mvi.ChatListItem
import com.arkivanov.decompose.ComponentContext
import presentation.componentCoroutineScope

interface ChatComponent: ComponentContext {
    val container: ChatContainer

    val onDrawerClick: () -> Unit
}

class RealChatComponent(
    componentCtx: ComponentContext,
    chatConfig: ChatListItem?,
    override val onDrawerClick: () -> Unit
) : ChatComponent, ComponentContext by componentCtx {
    override val container = ChatContainer(chatConfig, componentCoroutineScope)
}
