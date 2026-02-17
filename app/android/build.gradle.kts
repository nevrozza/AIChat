plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)

}

dependencies {
    implementation(libs.compose.foundation)
    implementation(projects.shared.root.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.decompose.core)
    implementation(libs.koin.android)
}

android {
    val config = Config.Android
    namespace = Config.namespace
    compileSdk = config.compileSdk

    defaultConfig {
        applicationId = Config.namespace
        minSdk = config.minSdk
        targetSdk = config.targetSdk
        with(Config.Version) {
            versionCode = code
            versionName = name
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

