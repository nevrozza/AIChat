package org.nevrozq.aichat.features.chats

import chats.ChatClientEvent
import chats.ChatListServerEvent
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import org.nevrozq.aichat.plugins.proto
import org.nevrozq.aichat.utils.sendWSResponse
import utils.api.Event
import utils.api.WSFrame

@OptIn(ExperimentalSerializationApi::class)
fun Route.chatRoutes() {
    val chatService: ChatsService = ChatsServiceImpl() // TODO: DI

    webSocket("/chat") {
        var subscriptionJob: Job? = null


        val job = launch {
            chatService.chats.collect { chatsInfo ->
                sendWSResponse(id = null, ChatListServerEvent.UpdateChatList(chatsInfo))
            }
        }

        try {
            for (frame in incoming) {
                if (frame is Frame.Binary) {

                    val inWSFrame = proto.decodeFromByteArray<WSFrame>(frame.data)

                    try {
                        when (val event = inWSFrame.event) {
                            is ChatClientEvent -> handleChatEvent(
                                event, inWSFrame.id, chatService,
                                onNewSubscribtion = { newJob ->
                                    subscriptionJob?.cancel()
                                    subscriptionJob = newJob
                                }
                            )
                        }
                    } catch (e: Exception) {
                        sendWSResponse(inWSFrame.id, Event.Error(e.message ?: "Unknown error"))
                    }

                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Connection closed: ${closeReason.await()}")
        } catch (e: Exception) {
            println("WS Global Error: ${e.message}")
        } finally {
            println("WS Closed")
            job.cancel()
            subscriptionJob?.cancel()
        }
    }
}