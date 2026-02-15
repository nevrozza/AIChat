package org.nevrozq.aichat.plugins

import io.ktor.serialization.kotlinx.protobuf.protobuf
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        protobuf(protobuf = proto)
    }
}