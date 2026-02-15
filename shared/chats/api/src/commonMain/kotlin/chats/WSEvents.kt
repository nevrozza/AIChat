package chats

import chats.ChatClientEvent.*
import chats.ChatListServerEvent.*
import chats.dtos.ChatInfoDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import utils.api.Event
import utils.api.Event.ClientEvent
import utils.api.Event.ServerEvent


val chatsSerializersModule = SerializersModule {
    polymorphic(Event::class) {
        subclass(UpdateChatList::class)

        subclass(CreateChat::class)
    }
}

@Serializable
sealed interface ChatListServerEvent : ServerEvent {
    @Serializable
    @SerialName("UpdateChatList")
    data class UpdateChatList(val chatsInfo: List<ChatInfoDTO>) : ChatListServerEvent
}

@Serializable
sealed interface ChatClientEvent : ClientEvent {
    @Serializable
    data class CreateChat(val title: String) : ChatClientEvent
}