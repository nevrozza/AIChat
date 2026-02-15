package chats.usecases

import network.MainSocket

class ConnectToChatsWSUseCases(
    private val mainSocket: MainSocket
) {
    fun connect(url: String) = mainSocket.connect(url)
    fun disconnect() = mainSocket.disconnect()
}