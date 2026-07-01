plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.core.database"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.coroutines.core)
            implementation(libs.datetime)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("AIEnglishDatabase") {
            packageName.set("com.aienglish.core.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
            version = 3
        }
    }
}
