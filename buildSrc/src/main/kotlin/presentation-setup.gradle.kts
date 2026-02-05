import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("shared-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose)
            implementation(libs.bundles.flowmvi)
        }
    }
}