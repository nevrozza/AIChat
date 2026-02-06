package chat

import chats.mvi.ChatListItem
import com.arkivanov.decompose.ComponentContext
import presentation.componentCoroutineScope

interface ChatComponent {
    val container: ChatContainer
}

class RealChatComponent(
    componentCtx: ComponentContext,
    chatConfig: ChatListItem?
) : ChatComponent, ComponentContext by componentCtx {
    override val container = ChatContainer(chatConfig, componentCoroutineScope)
}
