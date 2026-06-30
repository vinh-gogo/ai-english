package com.aienglish.core.common.tts

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.OfflineTtsConfig
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import kotlin.coroutines.resume

class AndroidTtsPlayer(
    private val context: Context,
    private val prefs: com.aienglish.core.common.prefs.AppPreferences
) : TtsPlayer {

    // Cache TTS instances rieng cho moi ngon ngu
    private var viTts: OfflineTts? = null
    private var enTts: OfflineTts? = null
    private var currentTtsSpeed: String? = null
    private var currentTtsVoice: String? = null
    private var mediaPlayer: android.media.MediaPlayer? = null

    private var audioTrack: AudioTrack? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var playJob: Job? = null

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    override fun play(text: String, languageCode: String) {
        val lang = languageCode.lowercase()
        if (lang != "vi" && lang != "en") {
            println("TTS for $languageCode is not supported on Android offline yet.")
            return
        }

        playJob?.cancel()
        stop()

        if (prefs.getTtsEngine() == "google") {
            playJob = scope.launch {
                try {
                    _isLoading.value = true
                    val encodedText = java.net.URLEncoder.encode(text, "UTF-8")
                    val url = "https://translate.google.com/translate_tts?ie=UTF-8&tl=$lang&client=tw-ob&q=$encodedText"
                    _isLoading.value = false
                    playUrl(url)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
            return
        }

        playJob = scope.launch {
            try {
                val currentJob = coroutineContext[Job]!!
                val speedSetting = prefs.getTtsSpeed()
                val voiceSetting = prefs.getTtsVoiceEn()

                // Reinit ca hai neu speed hoac voice thay doi
                if (currentTtsSpeed != speedSetting || currentTtsVoice != voiceSetting) {
                    viTts = null
                    enTts = null
                }

                // Copy assets lan dau (co the mat 5-15 giay cho ~130MB)
                val markerFile = File(context.filesDir, "vits/.copied")
                if (!markerFile.exists()) {
                    _isLoading.value = true
                }
                copyTtsAssetsIfNeeded()

                if (!currentJob.isActive) return@launch

                val currentTts = when (lang) {
                    "vi" -> {
                        if (viTts == null) {
                            _isLoading.value = true
                            viTts = initViTts(speedSetting)
                        }
                        viTts
                    }
                    "en" -> {
                        if (enTts == null) {
                            _isLoading.value = true
                            enTts = initEnTts(speedSetting)
                        }
                        enTts
                    }
                    else -> null
                } ?: run { _isLoading.value = false; return@launch }

                currentTtsSpeed = speedSetting
                currentTtsVoice = voiceSetting
                _isLoading.value = false

                if (!currentJob.isActive) return@launch

                val audio = currentTts.generate(text, 0)
                val sampleRate = audio.sampleRate
                val samples = audio.samples
                if (samples.isEmpty() || !currentJob.isActive) return@launch

                val shortSamples = ShortArray(samples.size) { i ->
                    (samples[i] * 32767.0f).toInt().coerceIn(-32768, 32767).toShort()
                }

                if (!currentJob.isActive) return@launch

                playShortSamples(shortSamples, sampleRate, currentJob)
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
                try {
                    for ((segText, segLang) in segments) {
                        val cleanText = cleanMarkdownSymbols(segText).trim()
                        if (cleanText.isEmpty()) continue
                        val encodedText = java.net.URLEncoder.encode(cleanText, "UTF-8")
                        val url = "https://translate.google.com/translate_tts?ie=UTF-8&tl=$segLang&client=tw-ob&q=$encodedText"
                        playUrl(url)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return
        }

        playJob = scope.launch {
            try {
                val currentJob = coroutineContext[Job]!!
                // Dam bao ca 2 engine san sang
                _isLoading.value = true
                copyTtsAssetsIfNeeded()
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

                // 4. Play combined samples in a single AudioTrack
                if (combinedSamples.isNotEmpty()) {
                    val shortSamples = ShortArray(combinedSamples.size) { i ->
                        (combinedSamples[i] * 32767.0f).toInt().coerceIn(-32768, 32767).toShort()
                    }
                    if (!currentJob.isActive) return@launch
                    playShortSamples(shortSamples, targetSampleRate, currentJob)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }



    // ── Language detection & splitting ──────────────────────────────────────────

    /**
     * Tach text thanh cac doan [text, lang] lien tiep.
     * Cac doan cung ngon ngu duoc gop lai de giam overhead.
     */
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



    // ── Copy assets ra filesDir (chỉ lần đầu, có marker file) ─────────────────

    private fun copyTtsAssetsIfNeeded() {
        val destDir = File(context.filesDir, "vits")
        val markerFile = File(destDir, ".copied")
        if (markerFile.exists()) return

        destDir.mkdirs()
        copyAssetDir(context.assets, "vits", destDir)
        markerFile.createNewFile()
    }

    private fun copyAssetDir(assets: android.content.res.AssetManager, assetPath: String, destDir: File) {
        val list = assets.list(assetPath) ?: return
        if (list.isEmpty()) {
            // Leaf file
            assets.open(assetPath).use { input ->
                val destFile = File(context.filesDir, assetPath)
                destFile.parentFile?.mkdirs()
                destFile.outputStream().use { output -> input.copyTo(output) }
            }
        } else {
            // Directory
            for (child in list) {
                val childAssetPath = "$assetPath/$child"
                val childDestDir = File(destDir, child)
                val childList = assets.list(childAssetPath) ?: emptyArray()
                if (childList.isEmpty()) {
                    childDestDir.parentFile?.mkdirs()
                    assets.open(childAssetPath).use { input ->
                        childDestDir.outputStream().use { output -> input.copyTo(output) }
                    }
                } else {
                    childDestDir.mkdirs()
                    copyAssetDir(assets, childAssetPath, childDestDir)
                }
            }
        }
    }

    // ── Khởi tạo TTS: dùng null AssetManager → newFromFile() ──────────────────
    // espeak_Initialize dùng POSIX I/O, KHÔNG đọc qua AssetManager stream

    private fun initViTts(speedSetting: String): OfflineTts {
        val lengthScale = speedLengthScale(speedSetting)
        val vitsDir = File(context.filesDir, "vits").absolutePath

        val vitsConfig = OfflineTtsVitsModelConfig().apply {
            model = "$vitsDir/adam1.onnx"
            tokens = "$vitsDir/tokens.txt"
            dataDir = "$vitsDir/espeak-ng-data"
            noiseScale = 0.667f
            noiseScaleW = 0.8f
            this.lengthScale = lengthScale
        }
        return buildTts(vitsConfig)
    }

    private fun initEnTts(speedSetting: String): OfflineTts {
        // Tiếng Anh: 1.3f = chuẩn (theo test research/audio-test/output-en-lessac.wav)
        val lengthScale = enLengthScale(speedSetting)
        val enDir = File(context.filesDir, "vits/en").absolutePath
        val espeakDir = File(context.filesDir, "vits/espeak-ng-data").absolutePath

        val voiceSetting = prefs.getTtsVoiceEn()
        val modelFile = if (voiceSetting == "amy") "en_US-amy-low.onnx" else "en_US-lessac-medium.onnx"

        val vitsConfig = OfflineTtsVitsModelConfig().apply {
            model = "$enDir/$modelFile"
            tokens = "$enDir/tokens.txt"
            dataDir = espeakDir
            noiseScale = 0.667f
            noiseScaleW = 0.8f
            this.lengthScale = lengthScale
        }
        return buildTts(vitsConfig)
    }

    private fun buildTts(vitsConfig: OfflineTtsVitsModelConfig): OfflineTts {
        val modelConfig = OfflineTtsModelConfig().apply {
            vits = vitsConfig
            numThreads = 2
            debug = false
        }
        val config = OfflineTtsConfig().apply { model = modelConfig }
        return OfflineTts(assetManager = null, config = config)
    }

    // ── Speed profiles ─────────────────────────────────────────────────────────

    private fun speedLengthScale(speedSetting: String) = when (speedSetting) {
        "slow"   -> 1.1f
        "normal" -> 0.85f
        "fast"   -> 0.7f
        else     -> 0.85f
    }

    /** English lessac */
    private fun enLengthScale(speedSetting: String) = when (speedSetting) {
        "slow"   -> 1.5f
        "normal" -> 1.3f   // user-confirmed
        "fast"   -> 1.1f
        else     -> 1.3f
    }

    // ── AudioTrack Stream playback ─────────────────────────────────────────────

    private fun playShortSamples(shortSamples: ShortArray, sampleRate: Int, job: Job) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack = track
        track.play()

        val chunkSize = minBufferSize / 2
        var offset = 0
        while (job.isActive && offset < shortSamples.size) {
            val length = minOf(chunkSize, shortSamples.size - offset)
            val written = track.write(shortSamples, offset, length)
            if (written <= 0) break
            offset += written
        }

        track.stop()
        track.release()
        audioTrack = null
    }

    override fun stop() {
        try {
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null

        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = null
    }

    private suspend fun playUrl(url: String) = suspendCancellableCoroutine<Unit> { continuation ->
        val mp = android.media.MediaPlayer().apply {
            setAudioAttributes(
                android.media.AudioAttributes.Builder()
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            val headers = mapOf("User-Agent" to "Mozilla/5.0")
            setDataSource(context, android.net.Uri.parse(url), headers)
            setOnPreparedListener { player ->
                player.start()
            }
            setOnCompletionListener { player ->
                player.release()
                if (continuation.isActive) continuation.resume(Unit)
            }
            setOnErrorListener { player, _, _ ->
                player.release()
                if (continuation.isActive) continuation.resume(Unit)
                true
            }
            prepareAsync()
        }
        mediaPlayer = mp
        continuation.invokeOnCancellation {
            try {
                mp.stop()
                mp.release()
            } catch (e: Exception) {}
            if (mediaPlayer == mp) {
                mediaPlayer = null
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
