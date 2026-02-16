package chats.ktor

import chats.ChatClientEvent
import chats.ChatListServerEvent
import chats.ChatServerEvent.*
import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
import chats.repositories.ChatListNetworkRepository
import chats.repositories.ChatNetworkRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import network.MainSocket
import network.send

class ChatsRemoteDataSource(
    private val httpClient: HttpClient,
    private val mainSocket: MainSocket
) : ChatListNetworkRepository, ChatNetworkRepository {
    override val chats: Flow<List<ChatInfoDTO>> = mainSocket.events
        .filterIsInstance<ChatListServerEvent.UpdateChatList>()
        .map { it.chatsInfo }
        .distinctUntilChanged() // update only if smth changed

    override suspend fun createChat(name: String): String {
        val chatCreatedEvent: ChatCreated =
            mainSocket.send(ChatClientEvent.CreateChat(name))
        return chatCreatedEvent.id
    }


    fun observeChat(chatId: String): Flow<List<ChatMessageDTO>> {
        return mainSocket.events
            .filter { event ->
                (event is ChatHistoryUpdate && event.chatId == chatId) ||
                        (event is NewMessage && event.message.chatId == chatId)
            }.distinctUntilChanged()
            .scan(emptyList()) { accumulator, event ->
                when (event) {
                    is ChatHistoryUpdate -> event.messages
                    is NewMessage -> {
                        val newMessage = event.message
                        val existingIndex = accumulator.indexOfFirst { it.id == newMessage.id }

                        if (existingIndex != -1) {
                            accumulator.toMutableList().apply {
                                set(existingIndex, newMessage)
                            }
                        } else {
                            accumulator + newMessage
                        }
                    }

                    else -> accumulator
                }
            }
    }


    suspend fun sendMessage(chatId: String, text: String) {
        mainSocket.sendRaw(ChatClientEvent.SendMessage(chatId = chatId, text = text))
    }

    suspend fun subscribeToChat(chatId: String) {
        mainSocket.sendRaw(ChatClientEvent.SubscribeToChat(chatId))
    }
}