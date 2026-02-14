package chats.usecases.chat

import chats.repositories.ChatRepository

class CreateChatUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(name: String) {
        if (name.isBlank()) throw IllegalArgumentException("Name cannot be empty")
        repository.createChat(name)
    }
}