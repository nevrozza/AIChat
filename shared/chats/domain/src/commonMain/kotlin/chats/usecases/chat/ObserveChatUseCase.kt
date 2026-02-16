package chats.usecases.chat

import chats.dtos.ChatMessageDTO
import chats.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveChatUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<ChatMessageDTO>> {
        return repository.observeChat(chatId)
    }
}