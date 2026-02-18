package chats.usecases.chat

import chats.dtos.ChatMessageDTO
import chats.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SendMessageUseCaseTest {

    private class FakeChatRepository : ChatRepository {
        var lastSentChatId: String? = null
        var lastSentText: String? = null

        override fun observeChat(chatId: String): Flow<List<ChatMessageDTO>> = emptyFlow()
        override suspend fun sendMessage(chatId: String, text: String) {
            lastSentChatId = chatId
            lastSentText = text
        }
        override suspend fun subscribeToChat(chatId: String) {}
        override suspend fun createChat(name: String): String = ""
    }

    @Test
    fun `when text is valid message is sent through repository`() = runTest {
        val repository = FakeChatRepository()
        val useCase = SendMessageUseCase(repository)

        useCase("chat_1", "Hello")

        assertEquals("chat_1", repository.lastSentChatId)
        assertEquals("Hello", repository.lastSentText)
    }

    @Test
    fun `when text is blank exception is thrown`() = runTest {
        val repository = FakeChatRepository()
        val useCase = SendMessageUseCase(repository)

        assertFailsWith<IllegalArgumentException> {
            useCase("chat_1", "  ")
        }
    }
}
