package chats.mvi


import chats.mvi.ChatsAction.SelectChat
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.enableLogging
import pro.respawn.flowmvi.plugins.init
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce

private typealias Ctx = PipelineContext<ChatsState, ChatsIntent, ChatsAction>

class ChatsContainer() : Container<ChatsState, ChatsIntent, ChatsAction> {

    override val store = store(initial = ChatsState.Loading) {
        configure {
            name = "Chats"
            debuggable = true
        }
        enableLogging()
        recover {
            updateState { ChatsState.Error(it) }
            null
        }
        init {
            loadChatsList()
        }
        reduce { intent ->
            when (intent) {
                is ChatsIntent.SetDrawerOpened -> {
                    action(ChatsAction.SetDrawerOpened(intent.isOpened))
                }

                is ChatsIntent.SelectedChat -> action(SelectChat(intent.id))
            }
        }
    }

    private fun Ctx.loadChatsList() = launch {
        updateState {
            ChatsState.OK(chats = listOf()) // TODO
        }
    }
}