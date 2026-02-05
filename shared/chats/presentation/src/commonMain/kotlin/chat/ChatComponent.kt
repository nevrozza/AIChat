package chat

import com.arkivanov.decompose.ComponentContext

interface ChatComponent

class RealChatComponent(
    componentContext: ComponentContext
) : ChatComponent, ComponentContext by componentContext {

}
