package chats

import chats.ChatClientEvent.CreateChat
import chats.ChatListServerEvent.UpdateChatList
import chats.ChatServerEvent.ChatCreated
import chats.dtos.ChatInfoDTO
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

        subclass(ChatCreated::class)
    }
}

@Serializable
sealed interface ChatListServerEvent : ServerEvent {
    @Serializable
    data class UpdateChatList(val chatsInfo: List<ChatInfoDTO>) : ChatListServerEvent
}

@Serializable
sealed interface ChatClientEvent : ClientEvent {
    @Serializable
    data class CreateChat(val title: String) : ChatClientEvent
}

@Serializable
sealed interface ChatServerEvent : ServerEvent {
    @Serializable
    data class ChatCreated(val id: String) : ChatServerEvent
}