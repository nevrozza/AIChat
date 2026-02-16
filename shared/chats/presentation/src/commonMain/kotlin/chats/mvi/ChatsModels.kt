package chats.mvi

import chats.entity.ChatListItem
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState


data class ChatsState(
    val url: String,
    val content: ChatsContent
) : MVIState {
    sealed interface ChatsContent : MVIState {
        data object Idle: ChatsContent
        data object Loading : ChatsContent
        data class Error(val e: Exception?) : ChatsContent
        data class OK(val chats: List<ChatListItem>) : ChatsContent
    }
}


sealed interface ChatsIntent : MVIIntent {
    data class SetDrawerOpened(val isOpened: Boolean) : ChatsIntent
    data class SelectedChat(val id: String?) : ChatsIntent

    data class ChangeServerUrl(val url: String) : ChatsIntent
    data object ClickedConnect : ChatsIntent
    data object ClickedDisconnect : ChatsIntent
}


sealed interface ChatsAction : MVIAction {
    data class SetDrawerOpened(val isOpened: Boolean) : ChatsAction

    data class SelectChat(val id: String?) : ChatsAction
}