package chats.entity

import chats.dtos.ChatInfoDTO

data class ChatListItem(
    val id: String,
    val title: String
)

fun ChatInfoDTO.toDomain() = ChatListItem(
    id = this.id,
    title = this.title
)

fun List<ChatInfoDTO>.listToDomain() = this.map { it.toDomain() }