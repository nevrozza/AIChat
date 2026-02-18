package org.nevrozq.aichat.features.chats.db

import chats.dtos.ChatInfoDTO

fun ChatEntity.Companion.getAllChatDTOs(): List<ChatInfoDTO> =
    all().map { it.toChatInfoDTO() }

fun ChatEntity.Companion.insert(newChat: ChatInfoDTO) {
    new(newChat.id) {
        title = newChat.title
    }
}