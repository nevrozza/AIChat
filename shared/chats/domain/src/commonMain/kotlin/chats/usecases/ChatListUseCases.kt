package chats.usecases

import chats.repositories.ChatListRepository
import chats.usecases.chatList.GetChatListUseCase

class ChatListUseCases(
    repository: ChatListRepository
) {
    val getChatList = GetChatListUseCase(repository)
}