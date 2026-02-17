package core.ktor

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow
import network.MainSocket
import org.koin.dsl.module
import utils.api.SocketState

internal val ktorModule = module {
    single<HttpClient> {
        getHttpClient(HttpEngineFactory().createEngine())
    }

    single<MainSocket> {
        MainSocketImpl(get())
    }

    single<StateFlow<SocketState>> {
        get<MainSocket>().state
    }
}