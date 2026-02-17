package chats.usecases.chat

import chats.entity.ChatMessage
import chats.entity.listToDomain
import chats.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveChatUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<ChatMessage>> {
        return repository.observeChat(chatId).map { it.listToDomain() }
    }
}