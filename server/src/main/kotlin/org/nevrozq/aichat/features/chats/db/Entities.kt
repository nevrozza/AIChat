package org.nevrozq.aichat.features.chats.db

import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

object ChatsTable : IdTable<String>("chats") {
    override val id: Column<EntityID<String>> = varchar("id", 256).entityId()
    val title = varchar("title", 256)
    override val primaryKey = PrimaryKey(id)
}

class ChatEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ChatEntity>(ChatsTable)

    var title by ChatsTable.title

    val messages by ChatMessageEntity referrersOn ChatMessagesTable.chatId

    fun toChatInfoDTO() = ChatInfoDTO(id = id.value, title = title)
}

object ChatMessagesTable : IdTable<String>("chat_messages") {
    override val id: Column<EntityID<String>> = varchar("id", 256).entityId()
    val chatId = reference("chat_id", ChatsTable)
    val text = text("text")
    val isFromUser = bool("is_from_user")
    val timestamp = long("timestamp")
    override val primaryKey = PrimaryKey(id)
}

class ChatMessageEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ChatMessageEntity>(ChatMessagesTable)

    var chat by ChatEntity referencedOn ChatMessagesTable.chatId
    var text by ChatMessagesTable.text
    var isFromUser by ChatMessagesTable.isFromUser
    var timestamp by ChatMessagesTable.timestamp

    fun toChatMessageDTO() = ChatMessageDTO(
        id = id.value,
        chatId = chat.id.value,
        text = text,
        isFromUser = isFromUser,
        timestamp = timestamp,
        isFull = true
    )
}