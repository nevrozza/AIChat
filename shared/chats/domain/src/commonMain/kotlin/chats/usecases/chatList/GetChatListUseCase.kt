package chats.usecases.chatList

import chats.entity.ChatListItem
import chats.entity.listToDomain
import kotlinx.coroutines.flow.Flow
import chats.repositories.ChatListRepository
import kotlinx.coroutines.flow.map

class GetChatListUseCase(
    private val repository: ChatListRepository
) {
    operator fun invoke(): Flow<List<ChatListItem>> {
        return repository.chats.map { it.listToDomain() }
    }
}