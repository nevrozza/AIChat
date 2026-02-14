package core.ktor

import core.ktor.sockets.MainSocket
import core.ktor.sockets.MainSocketImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module
import utils.api.SocketState

internal val ktorModule = module {
    single<HttpClient> {
        HttpClient(HttpEngineFactory().createEngine())
    }

    single<MainSocket> {
        MainSocketImpl(get())
    }

    single<StateFlow<SocketState>> {
        get<MainSocket>().state
    }
}