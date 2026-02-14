package chats.ktor

import chats.ChatClientEvent
import chats.ChatListServerEvent
import chats.dtos.ChatInfoDTO
import chats.repositories.ChatListNetworkRepository
import chats.repositories.ChatNetworkRepository
import core.ktor.sockets.MainSocket
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class ChatsRemoteDataSource(
    private val httpClient: HttpClient,
    private val mainSocket: MainSocket
) : ChatListNetworkRepository, ChatNetworkRepository {
    override val chats: Flow<List<ChatInfoDTO>> = mainSocket.events
        .filterIsInstance<ChatListServerEvent.UpdateChatList>()
        .map { it.chatsInfo }
        .distinctUntilChanged() // update only if smth changed

    override suspend fun createChat(name: String) {
        mainSocket.send(ChatClientEvent.CreateChat(name))
    }

}