package chats.usecases.chat

import chats.repositories.ChatRepository

class SendMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, text: String) {
        if (text.isBlank()) throw IllegalArgumentException("Message cannot be empty")
        repository.sendMessage(chatId, text)
    }
}