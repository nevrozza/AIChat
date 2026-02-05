@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    kotlin("plugin.serialization")
}

kotlin {
    androidLibrary {
        namespace = Config.Android.namespace(project.path)
        compileSdk = Config.Android.compileSdk
        androidResources.enable = true
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()
    listOf(js(IR), wasmJs()).forEach {
        it.browser()
    }

    applyDefaultHierarchyTemplate {
        common {
            group("skia") {
                withIosArm64()
                withIosSimulatorArm64()
                withJs()
                withWasmJs()
                withJvm()
            }
        }
    }
}