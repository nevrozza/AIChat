package core.ktor

import chats.ChatListServerEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.protobuf.protobuf
import kotlinx.serialization.ExperimentalSerializationApi
import utils.api.Event
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalSerializationApi::class)
fun getHttpClient(engineFactory: HttpClientEngineFactory<HttpClientEngineConfig>) =
    HttpClient(engineFactory) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 15000
            requestTimeoutMillis = 30000
        }

        install(ContentNegotiation) {
            protobuf(protobuf = proto)
        }

        install(WebSockets) {
            pingInterval = 15.seconds
            contentConverter = KotlinxWebsocketSerializationConverter(proto)
        }

        val eventSerializer = proto.serializersModule.getPolymorphic(Event::class, ChatListServerEvent.UpdateChatList::class.simpleName)

        println("DEBUG: Serializer found: ${eventSerializer != null}")
    }