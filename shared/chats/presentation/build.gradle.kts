plugins {
    id("presentation-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.chats.domain)
        }
    }
}