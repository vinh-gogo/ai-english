plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.feature.aiteacher.domain"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.arrow.core)
            implementation(project(":shared:core:common"))
        }
    }
}
