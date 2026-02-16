package chats.usecases.chat

import chats.repositories.ChatRepository

class SubscribeOnChatUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String) {
        repository.subscribeToChat(chatId)
    }
}