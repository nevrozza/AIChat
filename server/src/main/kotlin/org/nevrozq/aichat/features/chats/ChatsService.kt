package org.nevrozq.aichat.features.chats

import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
import chats.repositories.ChatListNetworkRepository
import chats.repositories.ChatNetworkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
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

    fun getHistory(chatId: String): List<ChatMessageDTO>
}

class ChatsServiceImpl : ChatsService {
    override val chats: MutableStateFlow<List<ChatInfoDTO>> = MutableStateFlow(emptyList())

    override val messageBus =
        MutableSharedFlow<ChatMessageDTO>(extraBufferCapacity = 64, replay = 30)

    private val chatHistory = mutableMapOf<String, MutableList<ChatMessageDTO>>() // TODO: db

    override suspend fun createChat(name: String): String {
        val newChat = ChatInfoDTO(id = UUID.randomUUID().toString(), title = name)
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
        chatHistory.getOrPut(chatId, ::mutableListOf)
        chatHistory[chatId]?.add(message)
        messageBus.emit(message)
    }

    override suspend fun emitToMessageBus(message: ChatMessageDTO) {
        messageBus.emit(message)
    }

    override fun getHistory(chatId: String): List<ChatMessageDTO> =
        chatHistory[chatId] ?: emptyList()
}