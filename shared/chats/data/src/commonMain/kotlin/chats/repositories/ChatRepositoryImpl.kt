package chats.repositories

import chats.dtos.ChatMessageDTO
import chats.ktor.ChatsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val chatsRemoteDataSource: ChatsRemoteDataSource
) : ChatRepository {
    override suspend fun createChat(name: String): String =
        chatsRemoteDataSource.createChat(name)

    override fun observeChat(chatId: String): Flow<List<ChatMessageDTO>> =
        chatsRemoteDataSource.observeChat(chatId)


    override suspend fun sendMessage(chatId: String, text: String) =
        chatsRemoteDataSource.sendMessage(chatId, text)

    override suspend fun subscribeToChat(chatId: String) =
        chatsRemoteDataSource.subscribeToChat(chatId)
}
