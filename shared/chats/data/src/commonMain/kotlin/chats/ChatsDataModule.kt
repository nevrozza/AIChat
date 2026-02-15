package chats

import chats.ktor.ChatsRemoteDataSource
import chats.repositories.ChatListRepository
import chats.repositories.ChatListRepositoryImpl
import chats.repositories.ChatRepository
import chats.repositories.ChatRepositoryImpl
import chats.usecases.ChatListUseCases
import chats.usecases.ChatUseCases
import chats.usecases.ConnectToChatsWSUseCases
import org.koin.dsl.module

val chatsDataModule = module {
    single<ChatsRemoteDataSource> { ChatsRemoteDataSource(get(), get()) }

    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<ChatListRepository> { ChatListRepositoryImpl(get()) }

    factory<ChatUseCases> {
        ChatUseCases(get())
    }
    factory<ChatListUseCases> {
        ChatListUseCases(get())
    }

    factory<ConnectToChatsWSUseCases> {
        ConnectToChatsWSUseCases(get())
    }
}