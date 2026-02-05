@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    listOf(js(IR), wasmJs()).forEach {
        with(it) {
            browser()
            binaries.executable()
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.root.compose)
            implementation(libs.compose.foundation)
        }
    }
}