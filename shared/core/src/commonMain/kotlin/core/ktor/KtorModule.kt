package core.ktor

import core.ktor.sockets.MainSocket
import core.ktor.sockets.MainSocketImpl
import io.ktor.client.HttpClient
import org.koin.dsl.module

internal val ktorModule = module {
    single<HttpClient> {
        HttpClient(HttpEngineFactory().createEngine())
    }

    single<MainSocket> {
        MainSocketImpl(get())
    }
}