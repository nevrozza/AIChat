import chats.mvi.ChatsContainer
import org.koin.dsl.module

val chatPresentationModule = module {
    factory<() -> ChatsContainer> {
        ::ChatsContainer
    }
}