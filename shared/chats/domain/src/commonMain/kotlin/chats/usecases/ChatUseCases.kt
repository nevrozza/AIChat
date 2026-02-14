package chats.usecases

import chats.repositories.ChatRepository
import chats.usecases.chat.CreateChatUseCase

class ChatUseCases(
    repository: ChatRepository
) {
    val createChat = CreateChatUseCase(repository)
}