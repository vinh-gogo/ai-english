import java.net.URI
import java.io.File

plugins {
    java
    application
}

repositories {
    mavenCentral()
}

val downloadLibs by tasks.registering {
    doLast {
        val libDir = file("lib")
        if (!libDir.exists()) libDir.mkdirs()
        
        val filesToDownload = mapOf(
            "sherpa-onnx-v1.12.10.jar" to "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.10/sherpa-onnx-v1.12.10.jar",
            "sherpa-onnx-native-lib-win-x64-v1.12.10.jar" to "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.10/sherpa-onnx-native-lib-win-x64-v1.12.10.jar"
        )
        
        filesToDownload.forEach { (name, urlString) ->
            val dest = File(libDir, name)
            if (!dest.exists()) {
                println("Downloading $name...")
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
            } else {
                println("$name already exists, skipping.")
            }
        }
    }
}

val downloadEspeakData by tasks.registering {
    doLast {
        val destFile = file("espeak-ng-data.tar.bz2")
        val destDir = file("espeak-ng-data")
        if (!destDir.exists()) {
            if (!destFile.exists()) {
                println("Downloading espeak-ng-data.tar.bz2...")
                try {
                    val url = URI.create("https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/espeak-ng-data.tar.bz2").toURL()
                    url.openStream().use { input ->
                        destFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    println("Downloaded espeak-ng-data.tar.bz2 successfully!")
                } catch (e: Exception) {
                    println("Error downloading espeak-ng-data: ${e.message}")
                    throw e
                }
            }
            println("Extracting espeak-ng-data.tar.bz2 using native 'tar' tool...")
            val process = ProcessBuilder("tar", "-xf", destFile.absolutePath)
                .directory(file("."))
                .inheritIO()
                .start()
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("tar command failed with exit code $exitCode")
            }
            println("Extracted espeak-ng-data successfully!")
            destFile.delete()
        } else {
            println("espeak-ng-data already exists, skipping.")
        }
    }
}

val downloadEnglishModel by tasks.registering {
    doLast {
        val destFile = file("vits-piper-en_US-amy-low.tar.bz2")
        val destDir = file("vits-piper-en_US-amy-low")
        if (!destDir.exists()) {
            if (!destFile.exists()) {
                println("Downloading vits-piper-en_US-amy-low.tar.bz2...")
                try {
                    val url = URI.create("https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/vits-piper-en_US-amy-low.tar.bz2").toURL()
                    url.openStream().use { input ->
                        destFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    println("Downloaded English model successfully!")
                } catch (e: Exception) {
                    println("Error downloading English model: ${e.message}")
                    throw e
                }
            }
            println("Extracting English model using native 'tar' tool...")
            val process = ProcessBuilder("tar", "-xf", destFile.absolutePath)
                .directory(file("."))
                .inheritIO()
                .start()
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("tar command failed with exit code $exitCode")
            }
            println("Extracted English model successfully!")
            destFile.delete()
        } else {
            println("English model already exists, skipping.")
        }
    }
}

tasks.named("compileJava") {
    dependsOn(downloadLibs)
}

tasks.named("run") {
    dependsOn(downloadLibs, downloadEspeakData, downloadEnglishModel)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8")
}

dependencies {
    implementation(fileTree("lib") { include("*.jar") })
}

application {
    mainClass.set("com.aienglish.research.AudioTest")
}
