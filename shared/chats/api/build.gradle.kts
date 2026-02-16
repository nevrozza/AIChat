plugins {
    id("api-domain-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.content.negotiation)
        }
    }
}