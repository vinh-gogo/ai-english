import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.aienglish.app"
    compileSdk = 35
    buildFeatures { buildConfig = true }
    defaultConfig {
        applicationId = "com.aienglish.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        buildConfigField(
            "String", "GEMINI_API_KEY",
            "\"${localProperties.getProperty("GEMINI_API_KEY", "")}\""
        )
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(libs.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.coroutines.android)
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // All shared modules
    implementation(project(":shared:core:common"))
    implementation(project(":shared:core:database"))
    implementation(project(":shared:core:network"))
    implementation(project(":shared:core:designsystem"))
    implementation(project(":shared:feature:ai-teacher:domain"))
    implementation(project(":shared:feature:ai-teacher:application"))
    implementation(project(":shared:feature:ai-teacher:data"))
    implementation(project(":shared:feature:ai-teacher:ui"))
    implementation(project(":shared:feature:profile:domain"))
    implementation(project(":shared:feature:profile:data"))
    implementation(project(":shared:feature:profile:ui"))
    implementation(project(":shared:feature:history:domain"))
    implementation(project(":shared:feature:history:data"))
    implementation(project(":shared:feature:history:ui"))
}
