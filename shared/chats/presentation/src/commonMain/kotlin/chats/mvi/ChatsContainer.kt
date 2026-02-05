package chats.mvi


import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce

import chats.mvi.ChatsState.*
import chats.mvi.ChatsAction.*
import kotlinx.coroutines.CoroutineScope

private typealias Ctx = PipelineContext<ChatsState, ChatsIntent, ChatsAction>

class ChatsContainer(
    coroutineScope: CoroutineScope
) : Container<ChatsState, ChatsIntent, ChatsAction> {

    override val store = store(initial = ChatsState(), scope = coroutineScope) {
        recover {
            updateState { copy(content = Content.Error(it)) }
            null
        }
        init {
            loadChatsList()
        }
        reduce { intent ->
            when (intent) {
                ChatsIntent.OpenedChats -> updateState { copy(isChatsOpened = !isChatsOpened) }
                is ChatsIntent.SelectedChat -> action(SelectChat(intent.id))
            }
        }
    }

    private fun Ctx.loadChatsList() = launch {
        updateState {
            copy(content = Content.OK(chats = listOf())) // TODO
        }
    }
}