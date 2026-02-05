package chats

import presentation.DefaultStack
import chat.ChatComponent
import chats.ChatsComponent.Child
import chats.ChatsComponent.Config
import kotlinx.serialization.Serializable

interface ChatsComponent: DefaultStack<Config, Child> {

    sealed interface Child {
        class ChatChild(val component: ChatComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data class Chat(val id: String?) : Config
    }
}