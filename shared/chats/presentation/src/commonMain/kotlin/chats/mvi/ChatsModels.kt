package chats.mvi

import chats.entity.ChatListItem
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState



// immutability?
sealed interface ChatsState : MVIState {
    data object Loading : ChatsState
    data class Error(val e: Exception?) : ChatsState
    data class OK(val chats: List<ChatListItem>) : ChatsState
}


sealed interface ChatsIntent : MVIIntent {
    data class SetDrawerOpened(val isOpened: Boolean) : ChatsIntent
    data class SelectedChat(val id: String?) : ChatsIntent
}

// immutability?
sealed interface ChatsAction : MVIAction {
    data class SetDrawerOpened(val isOpened: Boolean) : ChatsAction

    data class SelectChat(val id: String?) : ChatsAction
}