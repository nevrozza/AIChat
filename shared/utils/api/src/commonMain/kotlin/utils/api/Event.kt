package utils.api

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import utils.api.Event.AllGucci
import utils.api.Event.ClientEvent
import utils.api.Event.ServerEvent


val utilsSerializersModule = SerializersModule {
    polymorphic(Event::class) {
        subclass(ServerEvent::class)
        subclass(ClientEvent::class)
        subclass(AllGucci::class)
        subclass(Event.Error::class)
    }
}



// `@Serializable(with = PolymorphicSerializer::class)` to make it works on WASM

interface Event {
    @Serializable(with = PolymorphicSerializer::class)
    interface ServerEvent : Event
    @Serializable(with = PolymorphicSerializer::class)
    interface ClientEvent : Event


    @Serializable
    data object AllGucci : Event

    @Serializable
    data class Error(val message: String) : Event
}


