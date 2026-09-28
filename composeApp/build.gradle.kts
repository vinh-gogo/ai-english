plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(libs.coroutines.swing)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.datetime)
                implementation(project(":shared:core:common"))
                implementation(project(":shared:core:database"))
                implementation(project(":shared:core:network"))
                implementation(project(":shared:core:designsystem"))
                implementation(project(":shared:feature:ai-teacher:domain"))
                implementation(project(":shared:feature:ai-teacher:application"))
                implementation(project(":shared:feature:ai-teacher:data"))
                implementation(project(":shared:feature:ai-teacher:ui"))
                implementation(project(":shared:feature:profile:domain"))
                implementation(project(":shared:feature:profile:application"))
                implementation(project(":shared:feature:profile:data"))
                implementation(project(":shared:feature:profile:ui"))
                implementation(project(":shared:feature:history:domain"))
                implementation(project(":shared:feature:history:application"))
                implementation(project(":shared:feature:history:data"))
                implementation(project(":shared:feature:history:ui"))
                implementation(project(":shared:feature:pro:domain"))
                implementation(project(":shared:feature:pro:application"))
                implementation(project(":shared:feature:pro:data"))
                implementation(project(":shared:feature:pro:ui"))
                implementation(project(":shared:feature:vocabulary:domain"))
                implementation(project(":shared:feature:vocabulary:application"))
                implementation(project(":shared:feature:vocabulary:data"))
                implementation(project(":shared:feature:vocabulary:ui"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.aienglish.MainKt"
    }
}
