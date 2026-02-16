package org.nevrozq.aichat

import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.streaming.StreamFrame
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.send
import kotlinx.coroutines.delay
import org.nevrozq.aichat.features.allRoutes
import org.nevrozq.aichat.plugins.configureCORS
import org.nevrozq.aichat.plugins.configureKoog
import org.nevrozq.aichat.plugins.configureNegotiation
import org.nevrozq.aichat.plugins.configureWebSockets
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    configureNegotiation()
    configureCORS()
    configureKoog()
    configureWebSockets()

    allRoutes()


    routing {
        webSocket("/ws") {
            send("Meow")
            delay(3.seconds)
            send("Meow2")
        }

        get("/") {
            call.respondText("Ktor works")
        }

        get("/ai") {
            val a = llm().executeStreaming(
                prompt = prompt("chat") {
                    user("Помоги мне пожалуйста! Скажи, почему писать бэк на котлине лучше чем на питоне")
                },
                model = OllamaModels.Alibaba.QWEN_3_06B,
            )
            var x = ""
            a.collect {
                when (it) {
                    is StreamFrame.Append -> {
                        println("MEOW: ${it.text}")
                        x += it.text
                    }

                    is StreamFrame.End -> {}
                    is StreamFrame.ToolCall -> {}
                }
            }

            call.respond(HttpStatusCode.OK, x)
        }
    }
}