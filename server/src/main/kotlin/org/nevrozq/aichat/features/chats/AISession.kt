package org.nevrozq.aichat.features.chats

import ai.koog.ktor.Koog
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.streaming.StreamFrame
import io.ktor.server.application.Application
import io.ktor.server.application.plugin

fun Application.llm(): PromptExecutor = plugin(Koog).promptExecutor
class AISession(
    private val llm: PromptExecutor
) {
    suspend fun stream(
        message: String,
        onNewFrame: suspend (String) -> Unit
    ) {
        val a = llm.executeStreaming(
            prompt = prompt("chat") {
                user(message)
            },
            model = OllamaModels.Alibaba.QWEN_3_06B,
        )
        a.collect {
            when (it) {
                is StreamFrame.Append -> {
                    onNewFrame(it.text)
                }

                is StreamFrame.End -> {}
                is StreamFrame.ToolCall -> {}
            }
        }
    }
}