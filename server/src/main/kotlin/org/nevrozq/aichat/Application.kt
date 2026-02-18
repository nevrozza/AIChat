package org.nevrozq.aichat

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.nevrozq.aichat.features.allRoutes
import org.nevrozq.aichat.plugins.configureCORS
import org.nevrozq.aichat.plugins.configureDatabases
import org.nevrozq.aichat.plugins.configureKoog
import org.nevrozq.aichat.plugins.configureNegotiation
import org.nevrozq.aichat.plugins.configureWebSockets

fun main() {
    embeddedServer(Netty, port = EnvVals.serverPort, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabases()
    configureNegotiation()
    configureCORS()
    configureKoog()
    configureWebSockets()

    allRoutes()


    routing {
        get("/") {
            call.respondText("Ktor works")
        }
    }
}
