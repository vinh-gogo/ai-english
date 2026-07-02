import java.util.Properties
import java.net.URI
import java.io.File

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
    implementation(project(":shared:feature:grammar-graph:domain"))
    implementation(project(":shared:feature:grammar-graph:application"))
    implementation(project(":shared:feature:grammar-graph:data"))
    implementation(project(":shared:feature:grammar-graph:ui"))
}

val prepareAndroidTtsAssets by tasks.registering {
    val assetsDir = project.layout.projectDirectory.dir("src/main/assets/vits").asFile
    val enAssetsDir = File(assetsDir, "en")
    doLast {
        if (!assetsDir.exists()) assetsDir.mkdirs()
        if (!enAssetsDir.exists()) enAssetsDir.mkdirs()

        // ── Tiếng Việt: NGHI-TTS Adam ──────────────────────────────────────────
        val adamFile = File(assetsDir, "adam1.onnx")
        val tokensFile = File(assetsDir, "tokens.txt")
        val espeakDir = File(assetsDir, "espeak-ng-data")

        if (!tokensFile.exists()) {
            println("Downloading VI tokens.txt...")
            val url = URI.create("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/tokens.txt").toURL()
            url.openStream().use { input -> tokensFile.outputStream().use { output -> input.copyTo(output) } }
        }
        if (!adamFile.exists()) {
            println("Downloading adam1.onnx...")
            val url = URI.create("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/adam1.onnx").toURL()
            url.openStream().use { input -> adamFile.outputStream().use { output -> input.copyTo(output) } }
        }

        // ── Tiếng Anh: Piper lessac-medium & amy-low ────────────────────────────
        val lessacFile  = File(enAssetsDir, "en_US-lessac-medium.onnx")
        val amyFile  = File(enAssetsDir, "en_US-amy-low.onnx")
        val enTokensFile = File(enAssetsDir, "tokens.txt")

        if (!enTokensFile.exists()) {
            println("Downloading EN tokens.txt (lessac)...")
            val url = URI.create("https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/tokens.txt").toURL()
            url.openStream().use { input -> enTokensFile.outputStream().use { output -> input.copyTo(output) } }
        }
        if (!lessacFile.exists()) {
            println("Downloading en_US-lessac-medium.onnx (~60MB)...")
            val url = URI.create("https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/en_US-lessac-medium.onnx").toURL()
            url.openStream().use { input -> lessacFile.outputStream().use { output -> input.copyTo(output) } }
        }
        if (!amyFile.exists()) {
            println("Downloading en_US-amy-low.onnx (~60MB)...")
            val url = URI.create("https://huggingface.co/csukuangfj/vits-piper-en_US-amy-low/resolve/main/en_US-amy-low.onnx").toURL()
            url.openStream().use { input -> amyFile.outputStream().use { output -> input.copyTo(output) } }
        }

        // ── espeak-ng-data (dùng chung cho cả VI và EN) ─────────────────────────
        if (!espeakDir.exists()) {
            val userHome = System.getProperty("user.home")
            val cacheEspeak = File(userHome, ".ai-english/espeak-ng-data")
            if (cacheEspeak.exists()) {
                println("Copying espeak-ng-data from desktop cache to Android assets...")
                project.copy { from(cacheEspeak); into(espeakDir) }
            } else {
                println("Downloading espeak-ng-data.tar.bz2...")
                val tarFile = File(temporaryDir, "espeak-ng-data.tar.bz2")
                val url = URI.create("https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/espeak-ng-data.tar.bz2").toURL()
                url.openStream().use { input -> tarFile.outputStream().use { output -> input.copyTo(output) } }
                val process = ProcessBuilder("tar", "-xf", tarFile.absolutePath, "-C", assetsDir.absolutePath).start()
                if (process.waitFor() != 0) throw RuntimeException("Failed to extract espeak-ng-data")
                tarFile.delete()
            }
        }
    }
}

tasks.matching { it.name == "preBuild" }.all {
    dependsOn(prepareAndroidTtsAssets)
}
