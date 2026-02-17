package chats.usecases

import chats.repositories.ChatRepository
import chats.usecases.chat.CreateChatUseCase
import chats.usecases.chat.ObserveChatUseCase
import chats.usecases.chat.SendMessageUseCase
import chats.usecases.chat.SubscribeOnChatUseCase

class ChatUseCases(
    repository: ChatRepository
) {
    val createChat = CreateChatUseCase(repository)

    val observeChat = ObserveChatUseCase(repository)

    val sendMessage = SendMessageUseCase(repository)

    val subscribeOnChat = SubscribeOnChatUseCase(repository)
}
