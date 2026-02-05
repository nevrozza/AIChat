package root

import architecture.DefaultStack
import chats.ChatsComponent
import kotlinx.serialization.Serializable
import root.RootComponent.Child
import root.RootComponent.Config

interface RootComponent : DefaultStack<Config, Child> {

    sealed interface Child {
        class ChatsChild(val component: ChatsComponent) : Child
    }

    @Serializable
    sealed interface Config {
        object Chats : Config
    }

}