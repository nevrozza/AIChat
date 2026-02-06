plugins {
    id("compose-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.chats.presentation)
            implementation(projects.shared.utils.compose)
        }
    }
}