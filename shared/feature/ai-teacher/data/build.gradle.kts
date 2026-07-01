plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.feature.aiteacher.data"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.arrow.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.datetime)
            implementation(libs.sqldelight.coroutines)
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:core:database"))
            implementation(project(":shared:feature:ai-teacher:domain"))
            implementation(project(":shared:feature:history:domain"))
        }
    }
}

sqldelight {
    databases {
        create("AiTeacherDatabase") {
            packageName.set("com.aienglish.feature.aiteacher.data.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
}
