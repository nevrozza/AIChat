package root

import architecture.DefaultStack
import kotlinx.serialization.Serializable
import root.RootComponent.Child
import root.RootComponent.Config

interface RootComponent : DefaultStack<Config, Child> {

    sealed interface Child {
        class ChatsChild(val component: Any) : Child
    }

    @Serializable
    sealed interface Config {
        object Chats : Config
    }

}