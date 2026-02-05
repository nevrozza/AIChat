rootProject.name = "AIChat"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


include(":app:android")
include(":app:ios:ios-compose")
include(":app:desktop-jvm")
include(":app:web")
include(":server")


include(":shared:core")

include(":shared:root:compose")
include(":shared:root:presentation")

include(":shared:chats:presentation")


include(":shared:utils:common")
include(":shared:utils:compose")