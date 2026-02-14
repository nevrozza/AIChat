package org.nevrozq.aichat.features.chats

import chats.entities.ChatInfoDTO
import chats.repositories.ChatListNetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ChatsService : ChatListNetworkRepository {
    override val chats: MutableStateFlow<List<ChatInfoDTO>> = MutableStateFlow(emptyList())

    override suspend fun createChat(name: String): ChatInfoDTO {
        val newChat = ChatInfoDTO(id = UUID.randomUUID().toString(), title = name)
        chats.update { currentList ->
            currentList + newChat
        }
        return newChat
    }
}