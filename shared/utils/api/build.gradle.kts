
plugins {
    id("shared-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.serialization)
        }
    }
}