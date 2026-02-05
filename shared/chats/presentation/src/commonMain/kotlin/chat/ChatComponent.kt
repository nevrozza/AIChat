package chat

import chats.mvi.ChatListItem
import com.arkivanov.decompose.ComponentContext
import presentation.componentCoroutineScope

interface ChatComponent

class RealChatComponent(
    componentCtx: ComponentContext,
    chatConfig: ChatListItem?
) : ChatComponent, ComponentContext by componentCtx {
    val container = ChatContainer(chatConfig, componentCoroutineScope)
}
