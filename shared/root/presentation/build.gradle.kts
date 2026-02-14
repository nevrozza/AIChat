plugins {
    id("presentation-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.chats.presentation)
            implementation(projects.shared.core)
        }
    }
}