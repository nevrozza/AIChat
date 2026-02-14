package chats

import chats.dtos.ChatInfoDTO
import utils.api.Event.ClientEvent
import utils.api.Event.ServerEvent

sealed interface ChatListServerEvent : ServerEvent {
    data class UpdateChatList(val chatsInfo: List<ChatInfoDTO>) : ChatListServerEvent
}

sealed interface ChatClientEvent : ClientEvent {
    data class CreateChat(val title: String) : ChatClientEvent
}