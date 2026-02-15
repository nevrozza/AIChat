package core.ktor

import chats.chatsSerializersModule
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import utils.api.utilsSerializersModule

@OptIn(ExperimentalSerializationApi::class)
val proto = ProtoBuf {
    this.serializersModule = SerializersModule {
        include(utilsSerializersModule)
        include(chatsSerializersModule)
    }
}