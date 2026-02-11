plugins {
    id("api-domain-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.chats.api)
        }
    }
}