package org.nevrozq.aichat.plugins

import ai.koog.ktor.Koog
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureKoog() {
    install(Koog) {
        llm {
            this.ollama {
                baseUrl = "http://localhost:11434"
            }

        }
    }
}