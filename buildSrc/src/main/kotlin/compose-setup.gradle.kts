import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("shared-setup")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")

}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiTooling)
        }

        commonMain.dependencies {
            implementation(libs.flowmvi.essenty.compose)
            implementation(libs.decompose.compose)


            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)
        }
    }


    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "com.arkivanov.decompose.ExperimentalDecomposeApi"
        )
    }

    composeCompiler {
        stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("stability_definitions.txt"))
    }
}