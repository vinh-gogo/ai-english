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
import java.io.File

class AndroidTtsPlayer(
    private val context: Context,
    private val prefs: com.aienglish.core.common.prefs.AppPreferences
) : TtsPlayer {
    private var tts: OfflineTts? = null
    private var currentTtsSpeed: String? = null
    private var audioTrack: AudioTrack? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun play(text: String, languageCode: String) {
        if (languageCode.lowercase() != "vi") {
            println("TTS for $languageCode is not supported on Android offline yet.")
            return
        }

        scope.launch {
            try {
                val speedSetting = prefs.getTtsSpeed()
                if (tts == null || currentTtsSpeed != speedSetting) {
                    // Đảm bảo assets đã được copy ra filesDir trước khi init
                    copyTtsAssetsIfNeeded()
                    initTts(speedSetting)
                    currentTtsSpeed = speedSetting
                }

                val currentTts = tts ?: return@launch
                val audio = currentTts.generate(text, 0)
                val sampleRate = audio.sampleRate
                val samples = audio.samples

                if (samples.isEmpty()) return@launch

                // Convert FloatArray PCM to ShortArray (same as AudioTest.java)
                val shortSamples = ShortArray(samples.size) { i ->
                    val value = (samples[i] * 32767.0f).toInt()
                    value.coerceIn(-32768, 32767).toShort()
                }

                stop()
                playShortSamples(shortSamples, sampleRate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Copy toàn bộ thư mục vits/ từ assets ra filesDir/vits/
     * để espeak_Initialize có thể đọc bằng đường dẫn tuyệt đối trên filesystem.
     * Chỉ copy 1 lần nếu chưa tồn tại (kiểm tra marker file).
     *
     * Xem: https://github.com/k2-fsa/sherpa-onnx/blob/master/android/
     *      SherpaOnnxTtsEngine/app/src/main/java/com/k2fsa/sherpa/onnx/tts/engine/TtsEngine.kt#L188
     */
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
            // Leaf file — copy
            assets.open(assetPath).use { input ->
                val destFile = File(destDir.parent!!, destDir.name).let {
                    // assetPath = "vits/espeak-ng-data/lang/vi" → destFile = filesDir/vits/espeak-ng-data/lang/vi
                    File(context.filesDir, assetPath)
                }
                destFile.parentFile?.mkdirs()
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } else {
            // Directory — recurse
            for (child in list) {
                val childAssetPath = "$assetPath/$child"
                val childDestDir = File(destDir, child)
                val childList = assets.list(childAssetPath) ?: emptyArray()
                if (childList.isEmpty()) {
                    // File
                    childDestDir.parentFile?.mkdirs()
                    assets.open(childAssetPath).use { input ->
                        childDestDir.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                } else {
                    // Sub-directory
                    childDestDir.mkdirs()
                    copyAssetDir(assets, childAssetPath, childDestDir)
                }
            }
        }
    }

    /**
     * Khởi tạo OfflineTts với đường dẫn tuyệt đối ra filesDir
     * (espeak_Initialize yêu cầu real filesystem path, không phải asset path)
     */
    private fun initTts(speedSetting: String) {
        val lengthScale = when (speedSetting) {
            "slow"   -> 1.20f
            "normal" -> 1.0f
            "fast"   -> 0.85f
            else     -> 1.15f
        }

        val vitsDir = File(context.filesDir, "vits").absolutePath

        val vitsConfig = OfflineTtsVitsModelConfig().apply {
            model = "$vitsDir/adam1.onnx"
            tokens = "$vitsDir/tokens.txt"
            dataDir = "$vitsDir/espeak-ng-data"
            noiseScale = 0.667f
            noiseScaleW = 0.8f
            this.lengthScale = lengthScale
        }

        val modelConfig = OfflineTtsModelConfig().apply {
            vits = vitsConfig
            numThreads = 2
            debug = false
        }

        val config = OfflineTtsConfig().apply {
            model = modelConfig
        }

        // Truyền null AssetManager → native gọi newFromFile() thay vì newFromAsset()
        // Giống hệt Desktop JAR: OfflineTts(config) — không có AssetManager
        // espeak_Initialize dùng POSIX I/O, không đọc được qua AssetManager stream
        tts = OfflineTts(assetManager = null, config = config)
    }

    /**
     * Phát ShortArray PCM qua AudioTrack.MODE_STREAM
     * (tránh crash OOM của MODE_STATIC khi buffer audio quá lớn)
     */
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

        // Ghi theo từng chunk (tương đương SourceDataLine.write() trên Desktop)
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
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
    }
}
