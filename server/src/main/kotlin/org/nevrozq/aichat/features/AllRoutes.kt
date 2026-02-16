package org.nevrozq.aichat.features

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.nevrozq.aichat.features.chats.chatRoutes

fun Application.allRoutes() {
    routing {
        chatRoutes()
    }
}