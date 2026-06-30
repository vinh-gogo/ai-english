package com.aienglish.core.common.tts

import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.OfflineTtsConfig
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URI
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

class DesktopTtsPlayer(
    private val prefs: com.aienglish.core.common.prefs.AppPreferences
) : TtsPlayer {
    private var tts: OfflineTts? = null
    private var currentTtsSpeed: String? = null
    private var line: SourceDataLine? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isDownloading = false

    private val dbDir = File(System.getProperty("user.home"), ".ai-english")
    private val modelsDir = File(dbDir, "models")
    private val espeakDir = File(dbDir, "espeak-ng-data")

    init {
        modelsDir.mkdirs()
    }

    override fun play(text: String, languageCode: String) {
        if (languageCode.lowercase() != "vi") {
            println("TTS for $languageCode is not supported on desktop yet.")
            return
        }

        scope.launch {
            try {
                if (!checkAndDownloadAssets()) {
                    println("Cannot play TTS: assets are downloading...")
                    return@launch
                }

                val speedSetting = prefs.getTtsSpeed()
                if (tts == null || currentTtsSpeed != speedSetting) {
                    val adamModelPath = File(modelsDir, "adam1.onnx").absolutePath
                    val tokensPath = File(modelsDir, "tokens.txt").absolutePath
                    val espeakPath = espeakDir.absolutePath

                    val lengthScale = when (speedSetting) {
                        "slow" -> 1.20f   // Slow
                        "normal" -> 1.0f  // Normal
                        "fast" -> 0.85f   // Fast
                        else -> 1.20f
                    }

                    val vitsConfig = OfflineTtsVitsModelConfig.builder()
                        .setModel(adamModelPath)
                        .setTokens(tokensPath)
                        .setDataDir(espeakPath)
                        .setNoiseScale(0.667f)
                        .setNoiseScaleW(0.8f)
                        .setLengthScale(lengthScale)
                        .build()

                    val modelConfig = OfflineTtsModelConfig.builder()
                        .setVits(vitsConfig)
                        .setNumThreads(2)
                        .setDebug(false)
                        .build()

                    val config = OfflineTtsConfig.builder()
                        .setModel(modelConfig)
                        .build()

                    tts = OfflineTts(config)
                    currentTtsSpeed = speedSetting
                }

                val currentTts = tts ?: return@launch
                val audio = currentTts.generate(text, 0)
                val sampleRate = audio.sampleRate.toFloat()
                val samples = audio.samples

                if (samples.isEmpty()) return@launch

                val byteBuffer = ByteArray(samples.size * 2)
                for (i in samples.indices) {
                    val value = (samples[i] * 32767.0f).coerceIn(-32768f, 32767f).toInt()
                    byteBuffer[i * 2] = (value and 0xFF).toByte()
                    byteBuffer[i * 2 + 1] = ((value shr 8) and 0xFF).toByte()
                }

                stop()

                val format = AudioFormat(sampleRate, 16, 1, true, false)
                line = AudioSystem.getSourceDataLine(format).apply {
                    open(format)
                    start()
                    write(byteBuffer, 0, byteBuffer.size)
                    drain()
                    close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun stop() {
        try {
            line?.apply {
                if (isOpen) {
                    stop()
                    flush()
                    close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        line = null
    }

    private fun checkAndDownloadAssets(): Boolean {
        synchronized(this) {
            val adamFile = File(modelsDir, "adam1.onnx")
            val tokensFile = File(modelsDir, "tokens.txt")
            
            if (adamFile.exists() && tokensFile.exists() && espeakDir.exists()) {
                return true
            }

            if (isDownloading) {
                return false
            }

            isDownloading = true
            scope.launch {
                try {
                    if (!tokensFile.exists()) {
                        downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/tokens.txt", tokensFile)
                    }

                    if (!adamFile.exists()) {
                        downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/adam1.onnx", adamFile)
                    }

                    if (!espeakDir.exists()) {
                        val tarFile = File(dbDir, "espeak-ng-data.tar.bz2")
                        downloadFile("https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/espeak-ng-data.tar.bz2", tarFile)
                        println("Extracting espeak-ng-data.tar.bz2 on Desktop...")
                        val process = ProcessBuilder("tar", "-xf", tarFile.absolutePath)
                            .directory(dbDir)
                            .start()
                        val exitCode = process.waitFor()
                        if (exitCode == 0) {
                            println("Extracted espeak-ng-data successfully!")
                            tarFile.delete()
                        } else {
                            println("Failed to extract espeak-ng-data. Exit code: $exitCode")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isDownloading = false
                }
            }
            return false
        }
    }

    private fun downloadFile(urlString: String, destination: File) {
        println("Downloading $urlString to ${destination.absolutePath}...")
        var currentUrl = urlString
        var conn: HttpURLConnection? = null
        for (i in 0..4) {
            val url = URI.create(currentUrl).toURL()
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            conn.instanceFollowRedirects = true
            
            val status = conn.responseCode
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == 307 || status == 308) {
                currentUrl = conn.getHeaderField("Location")
                conn.disconnect()
            } else {
                break
            }
        }

        if (conn == null || conn.responseCode != HttpURLConnection.HTTP_OK) {
            throw java.io.IOException("Failed to connect or HTTP not 200: ${conn?.responseCode}")
        }

        conn.inputStream.use { input ->
            BufferedInputStream(input).use { bis ->
                FileOutputStream(destination).use { fos ->
                    val buffer = ByteArray(1024 * 8)
                    var count: Int
                    while (bis.read(buffer).also { count = it } != -1) {
                        fos.write(buffer, 0, count)
                    }
                }
            }
        }
        println("Downloaded file successfully: ${destination.name}")
    }
}
