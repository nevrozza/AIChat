plugins {
    id("shared-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.utils.api)
            implementation(libs.decompose.core)

            implementation(libs.kotlinx.coroutines)

        }
    }
}

