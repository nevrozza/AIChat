plugins {
    id("data-ktor-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.chats.domain)
        }
    }
}