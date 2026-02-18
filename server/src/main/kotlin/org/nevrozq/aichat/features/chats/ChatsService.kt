package org.nevrozq.aichat.features.chats

import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
import chats.repositories.ChatListNetworkRepository
import chats.repositories.ChatNetworkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.nevrozq.aichat.features.chats.db.ChatEntity
import org.nevrozq.aichat.features.chats.db.ChatMessageEntity
import org.nevrozq.aichat.features.chats.db.getAllChatDTOs
import org.nevrozq.aichat.features.chats.db.getChatHistory
import org.nevrozq.aichat.features.chats.db.insert
import org.nevrozq.aichat.plugins.dbQuery
import java.util.UUID

interface ChatsService : ChatListNetworkRepository, ChatNetworkRepository {
    val messageBus: SharedFlow<ChatMessageDTO>

    suspend fun postMessage(
        id: String = UUID.randomUUID().toString(),
        chatId: String,
        text: String,
        isFromUser: Boolean
    )

    suspend fun emitToMessageBus(message: ChatMessageDTO)

    suspend fun getHistory(chatId: String): List<ChatMessageDTO>
}

class ChatsServiceImpl : ChatsService {
    override val chats: MutableStateFlow<List<ChatInfoDTO>> = MutableStateFlow(emptyList())

    override val messageBus =
        MutableSharedFlow<ChatMessageDTO>(extraBufferCapacity = 64)

    init {
        runBlocking {
            chats.value = dbQuery {
                ChatEntity.getAllChatDTOs()
            }
        }
    }

    override suspend fun createChat(name: String): String {
        val newChat = ChatInfoDTO(id = UUID.randomUUID().toString(), title = name)
        dbQuery {
            ChatEntity.insert(newChat)
        }
        chats.update { currentList ->
            currentList + newChat
        }
        return newChat.id
    }

    override suspend fun postMessage(
        id: String,
        chatId: String,
        text: String,
        isFromUser: Boolean
    ) {
        val message = ChatMessageDTO(
            id = id,
            chatId = chatId,
            text = text,
            isFromUser = isFromUser,
            isFull = true
        )
        dbQuery {
            ChatMessageEntity.insert(message)
        }
        messageBus.emit(message)
    }

    override suspend fun emitToMessageBus(message: ChatMessageDTO) {
        messageBus.emit(message)
    }

    override suspend fun getHistory(chatId: String): List<ChatMessageDTO> =
        dbQuery {
            ChatMessageEntity.getChatHistory(chatId)
        }
}