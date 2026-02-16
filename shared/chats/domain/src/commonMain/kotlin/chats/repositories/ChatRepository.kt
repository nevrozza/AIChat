package chats.repositories

import chats.dtos.ChatMessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository : ChatNetworkRepository {

    fun observeChat(chatId: String): Flow<List<ChatMessageDTO>>

    suspend fun sendMessage(chatId: String, text: String)
    suspend fun subscribeToChat(chatId: String)
}