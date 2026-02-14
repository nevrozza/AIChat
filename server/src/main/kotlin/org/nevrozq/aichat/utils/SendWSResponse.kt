package org.nevrozq.aichat.utils

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import utils.api.Event
import utils.api.WSFrame

suspend inline fun <reified T : Event> DefaultWebSocketServerSession.sendWSResponse(id: String?, event: T) {
    sendSerialized(WSFrame(id = id, event = event))
}