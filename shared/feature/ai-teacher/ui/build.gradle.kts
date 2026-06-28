plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.feature.aiteacher.ui"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.coroutines.core)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.arrow.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:designsystem"))
            implementation(project(":shared:feature:ai-teacher:domain"))
            implementation(project(":shared:feature:ai-teacher:application"))
            implementation(project(":shared:feature:profile:ui"))
            implementation(project(":shared:feature:history:ui"))
            implementation(project(":shared:feature:history:domain"))
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.coroutines.swing)
            }
        }
    }
}
