plugins {
    id("compose-setup")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.root.presentation)
            api(projects.shared.utils.common)
            api(projects.shared.utils.compose)

            implementation(projects.shared.chats.compose)

        }
    }
}