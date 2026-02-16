package org.nevrozq.aichat.features.chats

import chats.ChatClientEvent
import chats.ChatServerEvent
import chats.repositories.ChatNetworkRepository
import io.ktor.server.websocket.DefaultWebSocketServerSession
import org.nevrozq.aichat.utils.sendWSResponse

suspend fun DefaultWebSocketServerSession.handleChatEvent(
    event: ChatClientEvent,
    requestId: String?,
    chatService: ChatNetworkRepository
) {
    when (event) {
        is ChatClientEvent.CreateChat -> {
            val id = chatService.createChat(event.title)
            sendWSResponse(requestId, ChatServerEvent.ChatCreated(id))
        }
    }
}