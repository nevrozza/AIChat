package chats.repositories

import chats.dtos.ChatInfoDTO
import kotlinx.coroutines.flow.Flow

interface ChatListNetworkRepository {
    val chats: Flow<List<ChatInfoDTO>>
}

