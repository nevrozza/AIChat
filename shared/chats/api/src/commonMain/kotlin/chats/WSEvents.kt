package chats

import chats.entities.ChatInfoDTO
import utils.api.Event.ClientEvent
import utils.api.Event.ServerEvent

sealed interface ChatServerEvent : ServerEvent {
    data class UpdateChatList(val chatsInfo: List<ChatInfoDTO>) : ChatServerEvent
}

sealed interface ChatClientEvent : ClientEvent {
    data class CreateChat(val title: String) : ChatClientEvent
}