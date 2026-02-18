package org.nevrozq.aichat.features.chats.db

import chats.dtos.ChatMessageDTO
import org.jetbrains.exposed.v1.core.eq

fun ChatMessageEntity.Companion.insert(message: ChatMessageDTO) {
    val chatRef = ChatEntity.findById(message.chatId)
        ?: throw IllegalArgumentException("Chat not found")

    new(message.id) {
        chat = chatRef
        text = message.text
        isFromUser = message.isFromUser
        timestamp = message.timestamp
    }
}

fun ChatMessageEntity.Companion.getChatHistory(chatId: String): List<ChatMessageDTO> =
    find { ChatMessagesTable.chatId eq chatId }
        .map { it.toChatMessageDTO() }
