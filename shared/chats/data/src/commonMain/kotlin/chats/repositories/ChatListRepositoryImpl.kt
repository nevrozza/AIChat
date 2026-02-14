package chats.repositories

import chats.dtos.ChatInfoDTO
import chats.ktor.ChatsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ChatListRepositoryImpl(
    private val chatsRemoteDataSource: ChatsRemoteDataSource
) : ChatListRepository {
    override val chats: Flow<List<ChatInfoDTO>> = chatsRemoteDataSource.chats
}