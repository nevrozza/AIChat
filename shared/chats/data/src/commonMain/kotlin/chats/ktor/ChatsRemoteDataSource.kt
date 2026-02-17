package chats.ktor

import chats.ChatClientEvent
import chats.ChatListServerEvent
import chats.ChatServerEvent
import chats.ChatServerEvent.ChatCreated
import chats.ChatServerEvent.ChatHistoryUpdate
import chats.dtos.ChatInfoDTO
import chats.dtos.ChatMessageDTO
import chats.repositories.ChatListNetworkRepository
import chats.repositories.ChatNetworkRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
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


    fun observeChat(chatId: String): Flow<List<ChatMessageDTO>> = channelFlow {
        // Запускаем подписку в отдельной корутине, чтобы не блокировать основной поток обработки
        launch {
            try {
                subscribeToChat(chatId)
            } catch (e: Exception) {
                println("Subscription error: ${e.message}")
            }
        }

        mainSocket.events
            .filter { event ->
                // Используем явные проверки типов, чтобы исключить ошибки импортов
                (event is ChatHistoryUpdate && event.chatId == chatId) ||
                        (event is ChatServerEvent.NewMessage && event.message.chatId == chatId)
            }
            .scan(emptyList<ChatMessageDTO>()) { accumulator, event ->
                println("DEBUG: observeChat processing event: $event")

                when (event) {
                    is ChatHistoryUpdate -> {
                        val history = event.messages
                        val historyIds = history.map { it.id }.toSet()
                        // Объединяем историю с тем, что уже могло прийти
                        history + accumulator.filter { it.id !in historyIds }
                    }

                    is ChatServerEvent.NewMessage -> {
                        val newMessage = event.message
                        val index = accumulator.indexOfFirst { it.id == newMessage.id }

                        if (index != -1) {
                            // Если сообщение уже есть (обновление LLM), заменяем его
                            accumulator.toMutableList().also {
                                it[index] = newMessage
                            }
                        } else {
                            // Если новое — добавляем в конец
                            accumulator + newMessage
                        }
                    }

                    else -> accumulator
                }
            }
            .collect { list ->
                send(list)
            }
    }


    suspend fun sendMessage(chatId: String, text: String) {
        mainSocket.sendRaw(ChatClientEvent.SendMessage(chatId = chatId, text = text))
    }

    suspend fun subscribeToChat(chatId: String) {
        mainSocket.sendRaw(ChatClientEvent.SubscribeToChat(chatId))
    }
}