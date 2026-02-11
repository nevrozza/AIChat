package chats.repositories

import chats.entities.ChatInfoDTO
import kotlinx.coroutines.flow.Flow

interface ChatListNetworkRepository {
    val chats: Flow<List<ChatInfoDTO>>
    suspend fun createChat(name: String): Result<ChatInfoDTO>
}

