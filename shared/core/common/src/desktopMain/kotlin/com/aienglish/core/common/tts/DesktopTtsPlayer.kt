package com.aienglish.core.common.tts

import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.OfflineTtsConfig
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    private var viTts: OfflineTts? = null
    private var enTts: OfflineTts? = null
    private var currentTtsSpeed: String? = null
    private var line: SourceDataLine? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isDownloading = false

    private val dbDir = File(System.getProperty("user.home"), ".ai-english")
    private val modelsDir = File(dbDir, "models")
    private val enModelsDir = File(modelsDir, "en")
    private val espeakDir = File(dbDir, "espeak-ng-data")

    init {
        modelsDir.mkdirs()
        enModelsDir.mkdirs()
    }

    override fun play(text: String, languageCode: String) {
        val lang = languageCode.lowercase()
        if (lang != "vi" && lang != "en") {
            println("TTS for $languageCode is not supported on desktop yet.")
            return
        }

        scope.launch {
            try {
                if (!checkAndDownloadAssets()) {
                    _isLoading.value = true
                    println("Cannot play TTS: assets are downloading...")
                    return@launch
                }

                val speedSetting = prefs.getTtsSpeed()
                if (currentTtsSpeed != speedSetting) {
                    viTts = null
                    enTts = null
                }

                val currentTts = when (lang) {
                    "vi" -> {
                        if (viTts == null) { _isLoading.value = true; viTts = initViTts(speedSetting) }
                        viTts
                    }
                    "en" -> {
                        if (enTts == null) { _isLoading.value = true; enTts = initEnTts(speedSetting) }
                        enTts
                    }
                    else -> null
                } ?: run { _isLoading.value = false; return@launch }

                currentTtsSpeed = speedSetting
                _isLoading.value = false

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
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun playMixed(text: String) {
        scope.launch {
            try {
                _isLoading.value = true
                if (!checkAndDownloadAssets()) return@launch
                val speedSetting = prefs.getTtsSpeed()
                if (currentTtsSpeed != speedSetting) { viTts = null; enTts = null }
                if (viTts == null) viTts = initViTts(speedSetting)
                if (enTts == null) enTts = initEnTts(speedSetting)
                currentTtsSpeed = speedSetting
                _isLoading.value = false

                stop()

                val segments = splitByLanguage(text)
                for ((segText, segLang) in segments) {
                    val tts = if (segLang == "vi") viTts else enTts
                    tts ?: continue
                    val audio = tts.generate(segText.trim(), 0)
                    if (audio.samples.isEmpty()) continue
                    val sampleRate = audio.sampleRate.toFloat()
                    val byteBuffer = ByteArray(audio.samples.size * 2)
                    for (i in audio.samples.indices) {
                        val v = (audio.samples[i] * 32767.0f).coerceIn(-32768f, 32767f).toInt()
                        byteBuffer[i * 2] = (v and 0xFF).toByte()
                        byteBuffer[i * 2 + 1] = ((v shr 8) and 0xFF).toByte()
                    }
                    val format = AudioFormat(sampleRate, 16, 1, true, false)
                    AudioSystem.getSourceDataLine(format).apply {
                        open(format); start()
                        write(byteBuffer, 0, byteBuffer.size)
                        drain(); close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun splitByLanguage(text: String): List<Pair<String, String>> {
        val chunks = text.split(Regex("(?<=[.!?])\\s+|\\n"))
            .map { it.trim() }.filter { it.isNotBlank() }
        val segments = mutableListOf<Pair<String, String>>()
        for (chunk in chunks) {
            val lang = if (isVietnamese(chunk)) "vi" else "en"
            if (segments.isNotEmpty() && segments.last().second == lang) {
                segments[segments.size - 1] = Pair(segments.last().first + " " + chunk, lang)
            } else {
                segments.add(Pair(chunk, lang))
            }
        }
        return segments
    }

    private fun isVietnamese(text: String): Boolean {
        val viChars = "àáảãạăắặẳẵâấầẩẫậđèéẻẽẹêếềểễệìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳýỷỹỵ" +
                "ÀÁẢÃẠĂẮẶẲẴÂẤẦẨẪẬĐÈÉẺẼẸÊẾỀỂỄỆÌÍỈĨỊÒÓỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÙÚỦŨỤƯỨỪỬỮỰỲÝỶỸỴ"
        return text.any { it in viChars }
    }



    private fun initViTts(speedSetting: String): OfflineTts {
        val lengthScale = when (speedSetting) {
            "slow"   -> 1.30f
            "normal" -> 1.15f
            "fast"   -> 1.0f
            else     -> 1.15f
        }
        val vitsConfig = OfflineTtsVitsModelConfig.builder()
            .setModel(File(modelsDir, "adam1.onnx").absolutePath)
            .setTokens(File(modelsDir, "tokens.txt").absolutePath)
            .setDataDir(espeakDir.absolutePath)
            .setNoiseScale(0.667f).setNoiseScaleW(0.8f)
            .setLengthScale(lengthScale).build()
        return OfflineTts(OfflineTtsConfig.builder()
            .setModel(OfflineTtsModelConfig.builder().setVits(vitsConfig).setNumThreads(2).setDebug(false).build())
            .build())
    }

    private fun initEnTts(speedSetting: String): OfflineTts {
        // 1.3f = bình thường theo user feedback (output-en-lessac.wav)
        val lengthScale = when (speedSetting) {
            "slow"   -> 1.5f
            "normal" -> 1.3f
            "fast"   -> 1.1f
            else     -> 1.3f
        }
        val vitsConfig = OfflineTtsVitsModelConfig.builder()
            .setModel(File(enModelsDir, "en_US-lessac-medium.onnx").absolutePath)
            .setTokens(File(enModelsDir, "tokens.txt").absolutePath)
            .setDataDir(espeakDir.absolutePath)
            .setNoiseScale(0.667f).setNoiseScaleW(0.8f)
            .setLengthScale(lengthScale).build()
        return OfflineTts(OfflineTtsConfig.builder()
            .setModel(OfflineTtsModelConfig.builder().setVits(vitsConfig).setNumThreads(2).setDebug(false).build())
            .build())
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
            val lessacFile = File(enModelsDir, "en_US-lessac-medium.onnx")
            val enTokensFile = File(enModelsDir, "tokens.txt")

            if (adamFile.exists() && tokensFile.exists() && lessacFile.exists() && enTokensFile.exists() && espeakDir.exists()) {
                return true
            }

            if (isDownloading) return false
            isDownloading = true

            scope.launch {
                try {
                    // Vietnamese
                    if (!tokensFile.exists()) downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/tokens.txt", tokensFile)
                    if (!adamFile.exists()) downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/adam1.onnx", adamFile)
                    // English
                    if (!enTokensFile.exists()) downloadFile("https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/tokens.txt", enTokensFile)
                    if (!lessacFile.exists()) downloadFile("https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/en_US-lessac-medium.onnx", lessacFile)
                    // espeak-ng-data
                    if (!espeakDir.exists()) {
                        val tarFile = File(dbDir, "espeak-ng-data.tar.bz2")
                        downloadFile("https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/espeak-ng-data.tar.bz2", tarFile)
                        println("Extracting espeak-ng-data...")
                        val process = ProcessBuilder("tar", "-xf", tarFile.absolutePath).directory(dbDir).start()
                        if (process.waitFor() == 0) { println("Extracted!"); tarFile.delete() }
                        else println("Failed to extract espeak-ng-data")
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
