package chats

import chats.ChatClientEvent.*
import chats.ChatListServerEvent.UpdateChatList
import chats.ChatServerEvent.*
import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
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
        subclass(SubscribeToChat::class)
        subclass(SendMessage::class)


        subclass(ChatCreated::class)
        subclass(ChatHistoryUpdate::class)
        subclass(NewMessage::class)
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

    @Serializable
    data class SubscribeToChat(val chatId: String) : ChatClientEvent

    @Serializable
    data class SendMessage(val chatId: String, val text: String) : ChatClientEvent
}

@Serializable
sealed interface ChatServerEvent : ServerEvent {
    @Serializable
    data class ChatCreated(val id: String) : ChatServerEvent

    @Serializable
    data class ChatHistoryUpdate(val chatId: String, val messages: List<ChatMessageDTO>) :
        ChatServerEvent

    @Serializable
    data class NewMessage(val message: ChatMessageDTO) : ChatServerEvent
}