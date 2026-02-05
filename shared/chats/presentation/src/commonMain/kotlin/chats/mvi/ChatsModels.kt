package chats.mvi

import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

data class ChatListItem(
    val id: String,
    val title: String
)

// immutability?
data class ChatsState(
    val isChatsOpened: Boolean = false,
    val content: Content = Content.Loading
) : MVIState {
    sealed interface Content {
        data object Loading : Content
        data class Error(val e: Exception?) : Content
        data class OK(val chats: List<ChatListItem>) : Content
    }
}

sealed interface ChatsIntent : MVIIntent {
    data object OpenedChats : ChatsIntent
    data class SelectedChat(val id: String) : ChatsIntent
}

// immutability?
sealed interface ChatsAction : MVIAction {
    data class SelectChat(val id: String) : ChatsAction
}