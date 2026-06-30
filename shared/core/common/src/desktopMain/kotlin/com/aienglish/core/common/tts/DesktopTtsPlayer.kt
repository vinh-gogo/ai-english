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
    private var currentTtsVoice: String? = null
    private var line: SourceDataLine? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isDownloading = false
    private var playJob: Job? = null
    private var googlePlayer: javazoom.jl.player.Player? = null

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

        playJob?.cancel()
        stop()

        if (prefs.getTtsEngine() == "google") {
            playJob = scope.launch {
                val currentJob = coroutineContext[Job]!!
                _isLoading.value = true
                playGoogleTts(text, lang, currentJob)
            }
            return
        }

        playJob = scope.launch {
            try {
                val currentJob = coroutineContext[Job]!!
                if (!checkAndDownloadAssets()) {
                    _isLoading.value = true
                    println("Cannot play TTS: assets are downloading...")
                    return@launch
                }

                val speedSetting = prefs.getTtsSpeed()
                val voiceSetting = prefs.getTtsVoiceEn()
                if (currentTtsSpeed != speedSetting || currentTtsVoice != voiceSetting) {
                    viTts = null
                    enTts = null
                }

                if (!currentJob.isActive) return@launch

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
                currentTtsVoice = voiceSetting
                _isLoading.value = false

                if (!currentJob.isActive) return@launch

                val audio = currentTts.generate(text, 0)
                val sampleRate = audio.sampleRate.toFloat()
                val samples = audio.samples
                if (samples.isEmpty() || !currentJob.isActive) return@launch

                val byteBuffer = ByteArray(samples.size * 2)
                for (i in samples.indices) {
                    val value = (samples[i] * 32767.0f).coerceIn(-32768f, 32767f).toInt()
                    byteBuffer[i * 2] = (value and 0xFF).toByte()
                    byteBuffer[i * 2 + 1] = ((value shr 8) and 0xFF).toByte()
                }

                if (!currentJob.isActive) return@launch

                val format = AudioFormat(sampleRate, 16, 1, true, false)
                val dataLine = AudioSystem.getSourceDataLine(format)
                line = dataLine
                dataLine.apply {
                    open(format)
                    start()
                    val chunkSize = 4096
                    var offset = 0
                    while (currentJob.isActive && offset < byteBuffer.size) {
                        val length = minOf(chunkSize, byteBuffer.size - offset)
                        val written = write(byteBuffer, offset, length)
                        if (written <= 0) break
                        offset += written
                    }
                    drain()
                    close()
                }
                line = null
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun playMixed(text: String) {
        playJob?.cancel()
        stop()

        val isJsonArray = text.trim().startsWith("[") && text.trim().endsWith("]")
        val segments = if (isJsonArray) {
            ExplanationParser.parseSegments(text)
        } else {
            splitByLanguage(text)
        }

        if (prefs.getTtsEngine() == "google") {
            playJob = scope.launch {
                val currentJob = coroutineContext[Job]!!
                for ((segText, segLang) in segments) {
                    if (!currentJob.isActive) break
                    val cleanText = cleanMarkdownSymbols(segText).trim()
                    if (cleanText.isEmpty()) continue
                    _isLoading.value = true
                    playGoogleTts(cleanText, segLang, currentJob)
                }
            }
            return
        }

        playJob = scope.launch {
            try {
                val currentJob = coroutineContext[Job]!!
                _isLoading.value = true
                if (!checkAndDownloadAssets()) return@launch
                val speedSetting = prefs.getTtsSpeed()
                val voiceSetting = prefs.getTtsVoiceEn()
                if (currentTtsSpeed != speedSetting || currentTtsVoice != voiceSetting) { viTts = null; enTts = null }
                if (viTts == null) viTts = initViTts(speedSetting)
                if (enTts == null) enTts = initEnTts(speedSetting)
                currentTtsSpeed = speedSetting
                currentTtsVoice = voiceSetting
                _isLoading.value = false

                if (!currentJob.isActive) return@launch

                // 1. Generate audio for all segments
                val generatedChunks = mutableListOf<Triple<FloatArray, Int, String>>()
                for ((segText, segLang) in segments) {
                    if (!currentJob.isActive) break
                    val cleanText = cleanMarkdownSymbols(segText).trim()
                    if (cleanText.isEmpty()) continue
                    val tts = if (segLang == "vi") viTts else enTts
                    tts ?: continue
                    
                    val textForTts = if (shouldAppendContinuation(segText)) {
                        "$cleanText,"
                    } else {
                        cleanText
                    }
                    
                    val audio = tts.generate(textForTts, 0)
                    if (audio.samples.isNotEmpty()) {
                        generatedChunks.add(Triple(audio.samples, audio.sampleRate, segText))
                    }
                }

                if (!currentJob.isActive || generatedChunks.isEmpty()) return@launch

                // 2. Determine target sample rate (use maximum sample rate)
                val targetSampleRate = generatedChunks.maxOf { it.second }

                // 3. Resample, trim silence, and add natural pauses where punctuation exists
                var totalLength = 0
                val processedChunks = generatedChunks.map { (samples, rate, originalText) ->
                    // A. Trim leading/trailing silence from the model output
                    var processed = trimSilence(samples, 0.005f)
                    
                    // B. Resample if rate doesn't match target
                    if (rate != targetSampleRate) {
                        processed = resample(processed, rate, targetSampleRate)
                    }
                    
                    // C. Add artificial pause for sentence boundaries
                    val trimmedText = originalText.trim()
                    val lastChar = trimmedText.lastOrNull()
                    if (lastChar != null) {
                        if (lastChar == '.' || lastChar == '?' || lastChar == '!' || lastChar == '\n') {
                            processed = appendSilence(processed, 0.3f, targetSampleRate)
                        } else if (lastChar == ':' || lastChar == ';') {
                            processed = appendSilence(processed, 0.15f, targetSampleRate)
                        } else if (lastChar == ',') {
                            processed = appendSilence(processed, 0.1f, targetSampleRate)
                        }
                    }
                    totalLength += processed.size
                    processed
                }

                val combinedSamples = FloatArray(totalLength)
                var destPos = 0
                for (chunk in processedChunks) {
                    System.arraycopy(chunk, 0, combinedSamples, destPos, chunk.size)
                    destPos += chunk.size
                }

                // 4. Play combined samples in a single SourceDataLine
                if (combinedSamples.isNotEmpty() && currentJob.isActive) {
                    val sampleRate = targetSampleRate.toFloat()
                    val byteBuffer = ByteArray(combinedSamples.size * 2)
                    for (i in combinedSamples.indices) {
                        val v = (combinedSamples[i] * 32767.0f).coerceIn(-32768f, 32767f).toInt()
                        byteBuffer[i * 2] = (v and 0xFF).toByte()
                        byteBuffer[i * 2 + 1] = ((v shr 8) and 0xFF).toByte()
                    }
                    if (currentJob.isActive) {
                        val format = AudioFormat(sampleRate, 16, 1, true, false)
                        val dataLine = AudioSystem.getSourceDataLine(format)
                        line = dataLine
                        dataLine.apply {
                            open(format); start()
                            val chunkSize = 4096
                            var offset = 0
                            while (currentJob.isActive && offset < byteBuffer.size) {
                                val length = minOf(chunkSize, byteBuffer.size - offset)
                                val written = write(byteBuffer, offset, length)
                                if (written <= 0) break
                                offset += written
                            }
                            drain(); close()
                        }
                        line = null
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
        val targetLang = prefs.getLanguageTarget().lowercase()
        val viChars = "àáảãạăắặẳẵâấầẩẫậđèéẻẽẹêếềểễệìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳýỷỹỵ" +
                "ÀÁẢÃẠĂẮẶẲẴÂẤẦẨẪẬĐÈÉẺẼẸÊẾỀỂỄỆÌÍỈĨỊÒÓỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÙÚỦŨỤƯỨỪỬỮỰỲÝỶỸỴ"
        
        fun isVi(t: String): Boolean = t.any { it in viChars }

        // Nhan dien cum tu dac biet: **bold**, *italic*, `code`, "quotes", 'quotes', /phonetic/, (parentheses)
        val pattern = Regex("\\*\\*(.*?)\\*\\*|\\*(.*?)\\*|`([^`]+)`|\\\"([^\\\"]+)\\\"|'([^']+)'|/([^/\\n]+)/|\\(([^)]+)\\)")
        
        val segments = mutableListOf<Pair<String, String>>()
        var lastIdx = 0
        
        fun addPlainSegments(plainText: String) {
            val rawChunks = plainText.split(Regex("(?<=[.!?])\\s+|\\n"))
                .map { it.trim() }
                .filter { it.isNotBlank() }
            for (chunk in rawChunks) {
                val lang = if (isVi(chunk)) "vi" else targetLang
                segments.add(Pair(chunk, lang))
            }
        }

        for (match in pattern.findAll(text)) {
            val start = match.range.first
            val end = match.range.last + 1
            
            if (start > lastIdx) {
                val plainText = text.substring(lastIdx, start)
                addPlainSegments(plainText)
            }
            
            val content = match.groupValues.firstOrNull { it.isNotEmpty() && it != match.value } ?: match.value
            val cleanedContent = content.trim()
            if (cleanedContent.isNotEmpty()) {
                val lang = if (isVi(cleanedContent)) "vi" else targetLang
                segments.add(Pair(cleanedContent, lang))
            }
            
            lastIdx = end
        }
        
        if (lastIdx < text.length) {
            val plainText = text.substring(lastIdx)
            addPlainSegments(plainText)
        }
        
        // Post-process: chuan hoa markdown, cat ngan neu dai hon 180 ky tu, gop cung ngon ngu
        val finalSegments = mutableListOf<Pair<String, String>>()
        for ((segText, segLang) in segments) {
            val cleaned = cleanMarkdownSymbols(segText).trim()
            if (cleaned.isEmpty()) continue
            
            val subPieces = splitIfTooLong(cleaned, 180)
            for (piece in subPieces) {
                val last = finalSegments.lastOrNull()
                if (last != null && last.second == segLang && (last.first.length + piece.length + 1) < 180) {
                    finalSegments[finalSegments.size - 1] = Pair(last.first + " " + piece, segLang)
                } else {
                    finalSegments.add(Pair(piece, segLang))
                }
            }
        }
        
        return finalSegments
    }

    private fun cleanMarkdownSymbols(text: String): String {
        return text
            .replace(Regex("\\*\\*|\\*|##?\\s?|`+"), "")
            .trim()
    }

    private fun splitIfTooLong(text: String, maxLen: Int): List<String> {
        if (text.length <= maxLen) return listOf(text)
        val subChunks = text.split(Regex("(?<=[,;])\\s+"))
        val result = mutableListOf<String>()
        var current = ""
        for (sub in subChunks) {
            if (current.isEmpty()) {
                current = sub
            } else if (current.length + sub.length + 1 <= maxLen) {
                current += ", " + sub
            } else {
                result.add(current)
                current = sub
            }
        }
        if (current.isNotEmpty()) {
            result.add(current)
        }
        return result
    }

    private fun isVietnamese(text: String): Boolean {
        val viChars = "àáảãạăắặẳẵâấầẩẫậđèéẻẽẹêếềểễệìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳýỷỹỵ" +
                "ÀÁẢÃẠĂẮẶẲẴÂẤẦẨẪẬĐÈÉẺẼẸÊẾỀỂỄỆÌÍỈĨỊÒÓỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÙÚỦŨỤƯỨỪỬỮỰỲÝỶỸỴ"
        return text.any { it in viChars }
    }



    private fun initViTts(speedSetting: String): OfflineTts {
        val lengthScale = when (speedSetting) {
            "slow"   -> 1.1f
            "normal" -> 0.85f
            "fast"   -> 0.7f
            else     -> 0.85f
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
        val voiceSetting = prefs.getTtsVoiceEn()
        val modelFileName = if (voiceSetting == "amy") "en_US-amy-low.onnx" else "en_US-lessac-medium.onnx"

        val vitsConfig = OfflineTtsVitsModelConfig.builder()
            .setModel(File(enModelsDir, modelFileName).absolutePath)
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

        try {
            googlePlayer?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        googlePlayer = null
    }

    private fun playGoogleTts(text: String, lang: String, job: Job) {
        try {
            val encodedText = java.net.URLEncoder.encode(text, "UTF-8")
            val urlString = "https://translate.google.com/translate_tts?ie=UTF-8&tl=$lang&client=tw-ob&q=$encodedText"
            val url = java.net.URI.create(urlString).toURL()
            val conn = url.openConnection()
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            
            val player = javazoom.jl.player.Player(conn.inputStream)
            googlePlayer = player
            
            if (job.isActive) {
                _isLoading.value = false
                player.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
            googlePlayer = null
        }
    }

    private fun checkAndDownloadAssets(): Boolean {
        synchronized(this) {
            val adamFile = File(modelsDir, "adam1.onnx")
            val tokensFile = File(modelsDir, "tokens.txt")
            
            val voiceSetting = prefs.getTtsVoiceEn()
            val targetEnModelFile = if (voiceSetting == "amy") File(enModelsDir, "en_US-amy-low.onnx") else File(enModelsDir, "en_US-lessac-medium.onnx")
            val enTokensFile = File(enModelsDir, "tokens.txt")

            if (adamFile.exists() && tokensFile.exists() && targetEnModelFile.exists() && enTokensFile.exists() && espeakDir.exists()) {
                return true
            }

            if (isDownloading) return false
            isDownloading = true

            scope.launch {
                try {
                    // Vietnamese
                    if (!tokensFile.exists()) downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/tokens.txt", tokensFile)
                    if (!adamFile.exists()) downloadFile("https://huggingface.co/doof-ferb/nghitts-copy/resolve/main/sherpa-onnx/adam1.onnx", adamFile)
                    
                    // English Tokens
                    if (!enTokensFile.exists()) {
                        val tokenUrl = if (voiceSetting == "amy") {
                            "https://huggingface.co/csukuangfj/vits-piper-en_US-amy-low/resolve/main/tokens.txt"
                        } else {
                            "https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/tokens.txt"
                        }
                        downloadFile(tokenUrl, enTokensFile)
                    }

                    // English Model
                    if (voiceSetting == "amy") {
                        if (!targetEnModelFile.exists()) downloadFile("https://huggingface.co/csukuangfj/vits-piper-en_US-amy-low/resolve/main/en_US-amy-low.onnx", targetEnModelFile)
                    } else {
                        if (!targetEnModelFile.exists()) downloadFile("https://huggingface.co/csukuangfj/vits-piper-en_US-lessac-medium/resolve/main/en_US-lessac-medium.onnx", targetEnModelFile)
                    }

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
    }

    private fun resample(samples: FloatArray, fromRate: Int, toRate: Int): FloatArray {
        if (fromRate == toRate) return samples
        val ratio = toRate.toDouble() / fromRate.toDouble()
        val newLength = (samples.size * ratio).toInt()
        val resampled = FloatArray(newLength)
        for (i in 0 until newLength) {
            val origIndex = i / ratio
            val index = origIndex.toInt()
            val frac = origIndex - index
            if (index + 1 < samples.size) {
                resampled[i] = (samples[index] * (1.0 - frac) + samples[index + 1] * frac).toFloat()
            } else if (index < samples.size) {
                resampled[i] = samples[index]
            }
        }
        return resampled
    }

    private fun trimSilence(samples: FloatArray, threshold: Float): FloatArray {
        var start = 0
        while (start < samples.size && kotlin.math.abs(samples[start]) < threshold) {
            start++
        }
        var end = samples.size - 1
        while (end >= start && kotlin.math.abs(samples[end]) < threshold) {
            end--
        }
        if (start > end) return FloatArray(0)
        return samples.copyOfRange(start, end + 1)
    }

    private fun appendSilence(samples: FloatArray, durationSeconds: Float, sampleRate: Int): FloatArray {
        val silenceLength = (sampleRate * durationSeconds).toInt()
        if (silenceLength <= 0) return samples
        val result = FloatArray(samples.size + silenceLength)
        System.arraycopy(samples, 0, result, 0, samples.size)
        return result
    }

    private fun shouldAppendContinuation(text: String): Boolean {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return false
        val lastChar = trimmed.last()
        return lastChar.isLetterOrDigit() || lastChar == '"' || lastChar == '\'' || lastChar == ')' || lastChar == ']'
    }
}
