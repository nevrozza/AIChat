import org.gradle.accessors.dm.LibrariesForLibs
val libs = the<LibrariesForLibs>()
plugins {
    id("shared-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // for flow
            implementation(libs.kotlinx.coroutines)

            api(project(":shared:utils:api"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
    }
}