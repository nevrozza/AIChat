package chats.repositories

import chats.ktor.ChatsRemoteDataSource

class ChatRepositoryImpl(
    private val chatsRemoteDataSource: ChatsRemoteDataSource
) : ChatRepository {
    override suspend fun createChat(name: String) {
        chatsRemoteDataSource.createChat(name)
    }
}