package chats

import presentation.DefaultStack
import chat.ChatComponent
import chats.ChatsComponent.Child
import chats.ChatsComponent.Config
import chats.mvi.ChatsAction
import chats.mvi.ChatsIntent
import chats.mvi.ChatsState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import pro.respawn.flowmvi.api.Store
import utils.api.SocketState

interface ChatsComponent : DefaultStack<Config, Child>,
    Store<ChatsState, ChatsIntent, ChatsAction>, ComponentContext {


    val socketState: StateFlow<SocketState>
    val uiEvents: Flow<UIEvent>

    sealed interface Child {
        class ChatChild(val component: ChatComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data class Chat(val id: String?) : Config
    }

    sealed interface UIEvent {
        data class SetDrawerOpened(val isOpened: Boolean) : UIEvent
    }
}