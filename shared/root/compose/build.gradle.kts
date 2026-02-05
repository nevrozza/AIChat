plugins {
    id("compose-setup")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.root.presentation)
        }
    }
}