import java.net.URI
import java.io.File

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
}

val downloadTtsLibs by tasks.registering {
    val libDir = project.layout.projectDirectory.dir("lib").asFile
    doLast {
        if (!libDir.exists()) libDir.mkdirs()
        
        val filesToDownload = mapOf(
            "sherpa-onnx-v1.12.10.jar" to "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.10/sherpa-onnx-v1.12.10.jar",
            "sherpa-onnx-native-lib-win-x64-v1.12.10.jar" to "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.10/sherpa-onnx-native-lib-win-x64-v1.12.10.jar",
            "sherpa-onnx-static-link-onnxruntime-1.12.10.aar" to "https://huggingface.co/csukuangfj/sherpa-onnx-libs/resolve/main/android/aar/1.12.10/sherpa-onnx-static-link-onnxruntime-1.12.10.aar"
        )
        
        filesToDownload.forEach { (name, urlString) ->
            val dest = File(libDir, name)
            if (!dest.exists()) {
                println("Downloading $name for TTS...")
                try {
                    val url = URI.create(urlString).toURL()
                    url.openStream().use { input ->
                        dest.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    println("Downloaded $name successfully!")
                } catch (e: Exception) {
                    println("Error downloading $name: ${e.message}")
                    throw e
                }
            }
        }
    }
}

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "com.aienglish.core.common"
        compileSdk = 35
        minSdk = 24
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.arrow.core)
            implementation(libs.koin.core)
            implementation(libs.napier)
        }
        androidMain.dependencies {
            implementation(libs.coroutines.android)
            implementation(libs.koin.android)
            implementation(libs.security.crypto)
            implementation(files("lib/sherpa-onnx-static-link-onnxruntime-1.12.10.aar"))
        }
        val desktopMain by getting {
            dependencies {
                implementation(fileTree("lib") { include("*.jar") })
            }
        }
    }
}

// Make compilation tasks depend on downloading libraries
tasks.configureEach {
    if (name.contains("compile") || name.contains("sourcesJar")) {
        dependsOn(downloadTtsLibs)
    }
}
