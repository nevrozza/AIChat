package chats.entity

import chats.dtos.ChatMessageDTO

data class ChatMessage(
    val id: String,
    val text: String,
    val isFromMe: Boolean
)


fun ChatMessageDTO.toDomain() = ChatMessage(
    text = this.text,
    isFromMe = this.isFromUser,
    id = this.id
)

fun List<ChatMessageDTO>.listToDomain() = this.map { it.toDomain() }