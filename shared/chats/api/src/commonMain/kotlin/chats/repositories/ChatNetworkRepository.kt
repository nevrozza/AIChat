package chats.repositories

interface ChatNetworkRepository {
    suspend fun createChat(name: String)
}