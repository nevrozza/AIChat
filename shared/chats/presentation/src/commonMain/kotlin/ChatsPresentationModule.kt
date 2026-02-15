import chats.mvi.ChatsContainer
import org.koin.dsl.module

val chatsPresentationModule = module {
    factory<() -> ChatsContainer> {
        { ChatsContainer(get(), get()) }
    }
}