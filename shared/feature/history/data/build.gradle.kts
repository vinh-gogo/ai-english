plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.feature.history.data"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.datetime)
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:database"))
            implementation(project(":shared:feature:history:domain"))
        }
    }
}
