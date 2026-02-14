package org.nevrozq.aichat.features.chats

import chats.ChatClientEvent
import chats.repositories.ChatNetworkRepository
import io.ktor.server.websocket.DefaultWebSocketServerSession
import org.nevrozq.aichat.utils.sendWSResponse
import utils.api.Event

suspend fun DefaultWebSocketServerSession.handleChatEvent(
    event: ChatClientEvent,
    requestId: String?,
    chatService: ChatNetworkRepository
) {
    when (event) {
        is ChatClientEvent.CreateChat -> {
            chatService.createChat(event.title)
            sendWSResponse(requestId, Event.AllGucci)
        }
    }
}