package chats.usecases.chatList

import app.cash.turbine.test
import chats.dtos.ChatInfoDTO
import chats.repositories.ChatListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetChatListUseCaseTest {

    private class FakeChatListRepository : ChatListRepository {
        private val _chats = MutableStateFlow<List<ChatInfoDTO>>(emptyList())
        override val chats: Flow<List<ChatInfoDTO>> = _chats

        fun emit(list: List<ChatInfoDTO>) {
            _chats.value = list
        }
    }

    @Test
    fun `when repository emits chats use case maps them to domain`() = runTest {
        val repository = FakeChatListRepository()
        val useCase = GetChatListUseCase(repository)

        val dtos = listOf(
            ChatInfoDTO(id = "1", title = "Chat 1"),
            ChatInfoDTO(id = "2", title = "Chat 2")
        )

        useCase().test {
            assertEquals(emptyList(), awaitItem())
            repository.emit(dtos)
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("1", result[0].id)
            assertEquals("Chat 1", result[0].title)
            assertEquals("2", result[1].id)
            assertEquals("Chat 2", result[1].title)
        }
    }
}
