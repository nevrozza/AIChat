package org.nevrozq.aichat.features.chats

import chats.ChatClientEvent
import chats.ChatServerEvent.ChatCreated
import chats.ChatServerEvent.ChatHistoryUpdate
import chats.ChatServerEvent.NewMessage
import chats.dtos.ChatMessageDTO
import io.ktor.server.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.nevrozq.aichat.utils.sendWSResponse
import java.util.UUID

suspend fun DefaultWebSocketServerSession.handleChatEvent(
    event: ChatClientEvent,
    requestId: String?,
    chatsService: ChatsService,
    onNewSubscribtion: (Job) -> Unit
) {
    when (event) {
        is ChatClientEvent.CreateChat -> {
            val id = chatsService.createChat(event.title)
            sendWSResponse(requestId, ChatCreated(id))
        }

        is ChatClientEvent.SendMessage -> {
            chatsService.postMessage(
                chatId = event.chatId,
                text = event.text,
                isFromUser = true
            )

            launch {
                simulateLlmStreaming(chatId = event.chatId, chatsService)
            }
        }

        is ChatClientEvent.SubscribeToChat -> {
            val history = chatsService.getHistory(event.chatId)
            sendWSResponse(requestId, ChatHistoryUpdate(event.chatId, history))

            onNewSubscribtion(
                launch {
                    chatsService.messageBus.collect { msg ->
                        if (msg.chatId == event.chatId) {
                            println("SERVER: Sending NewMessage to client: ${msg.text}")
                            sendWSResponse(null, NewMessage(msg))
                        }
                    }
                }
            )
        }
    }
}

suspend fun simulateLlmStreaming(chatId: String, chatService: ChatsService) {
    val fullResponse = "Это ооооооочеьн долгий ответ о о о о о о о о о о о о о о о о о о о о о о о о ответ от нейросети в реальном времени..."
    val messageId = UUID.randomUUID().toString()
    val sb = StringBuilder()

    fullResponse.split(" ").forEach { word ->
        delay(200)
        val part = "$word "
        sb.append(part)

        chatService.emitToMessageBus(
            ChatMessageDTO(
                id = messageId,
                chatId = chatId,
                text = sb.toString(),
                isFromUser = false,
                isFull = false
            )
        )
    }

    chatService.postMessage(id = messageId, chatId, sb.toString().trim(), false)

}