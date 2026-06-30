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

class AndroidTtsPlayer(
    private val context: Context,
    private val prefs: com.aienglish.core.common.prefs.AppPreferences
) : TtsPlayer {

    // Cache TTS instances rieng cho moi ngon ngu
    private var viTts: OfflineTts? = null
    private var enTts: OfflineTts? = null
    private var currentTtsSpeed: String? = null

    private var audioTrack: AudioTrack? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    override fun play(text: String, languageCode: String) {
        val lang = languageCode.lowercase()
        if (lang != "vi" && lang != "en") {
            println("TTS for $languageCode is not supported on Android offline yet.")
            return
        }

        scope.launch {
            try {
                val speedSetting = prefs.getTtsSpeed()

                // Reinit ca hai neu speed thay doi
                if (currentTtsSpeed != speedSetting) {
                    viTts = null
                    enTts = null
                }

                // Copy assets lan dau (co the mat 5-15 giay cho ~130MB)
                val markerFile = File(context.filesDir, "vits/.copied")
                if (!markerFile.exists()) {
                    _isLoading.value = true
                }
                copyTtsAssetsIfNeeded()

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
                _isLoading.value = false

                val audio = currentTts.generate(text, 0)
                val sampleRate = audio.sampleRate
                val samples = audio.samples
                if (samples.isEmpty()) return@launch

                val shortSamples = ShortArray(samples.size) { i ->
                    (samples[i] * 32767.0f).toInt().coerceIn(-32768, 32767).toShort()
                }

                stop()
                playShortSamples(shortSamples, sampleRate)
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
                // Dam bao ca 2 engine san sang
                _isLoading.value = true
                copyTtsAssetsIfNeeded()
                val speedSetting = prefs.getTtsSpeed()
                if (currentTtsSpeed != speedSetting) { viTts = null; enTts = null }
                if (viTts == null) viTts = initViTts(speedSetting)
                if (enTts == null) enTts = initEnTts(speedSetting)
                currentTtsSpeed = speedSetting
                _isLoading.value = false

                stop()

                // Phat tung doan theo ngon ngu
                val segments = splitByLanguage(text)
                for ((segText, segLang) in segments) {
                    val tts = if (segLang == "vi") viTts else enTts
                    tts ?: continue
                    val audio = tts.generate(segText.trim(), 0)
                    if (audio.samples.isNotEmpty()) {
                        val shortSamples = ShortArray(audio.samples.size) { i ->
                            (audio.samples[i] * 32767.0f).toInt().coerceIn(-32768, 32767).toShort()
                        }
                        // playShortSamples dung drain()nen block cho den het moi doan
                        playShortSamples(shortSamples, audio.sampleRate)
                    }
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
        // Tach theo cac ranh gioi cau va dong moi
        val chunks = text.split(Regex("(?<=[.!?])\\s+|\\n"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val segments = mutableListOf<Pair<String, String>>()
        for (chunk in chunks) {
            val lang = if (isVietnamese(chunk)) "vi" else "en"
            if (segments.isNotEmpty() && segments.last().second == lang) {
                // Gop voi doan truoc neu cung ngon ngu
                segments[segments.size - 1] = Pair(segments.last().first + " " + chunk, lang)
            } else {
                segments.add(Pair(chunk, lang))
            }
        }
        return segments
    }

    /**
     * Detect tieng Viet dua tren su ton tai cua ky tu co dau dac trung.
     * Tieng Anh chi dung ASCII, khong co nhung ky tu nay.
     */
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

        val vitsConfig = OfflineTtsVitsModelConfig().apply {
            model = "$enDir/en_US-lessac-medium.onnx"
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

    /** Vietnamese Adam */
    private fun speedLengthScale(speedSetting: String) = when (speedSetting) {
        "slow"   -> 1.5f
        "normal" -> 1.3f   // user-confirmed
        "fast"   -> 1.1f
        else     -> 1.3f
    }

    /** English lessac */
    private fun enLengthScale(speedSetting: String) = when (speedSetting) {
        "slow"   -> 1.5f
        "normal" -> 1.3f   // user-confirmed
        "fast"   -> 1.1f
        else     -> 1.3f
    }

    // ── AudioTrack Stream playback ─────────────────────────────────────────────

    private fun playShortSamples(shortSamples: ShortArray, sampleRate: Int) {
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
        while (offset < shortSamples.size) {
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
    }
}
