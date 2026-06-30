package com.aienglish.core.common.tts

import kotlinx.coroutines.flow.StateFlow

interface TtsPlayer {
    /** True khi dang copy model assets hoac khoi tao TTS engine lan dau */
    val isLoading: StateFlow<Boolean>

    /** Phat mot doan van ban voi ngon ngu xac dinh */
    fun play(text: String, languageCode: String)

    /**
     * Phat van ban hon hop tieng Anh + tieng Viet:
     * Tu dong detect ngon ngu tung cau va phat tuan tu bang giong phu hop.
     * Cau co dau tieng Viet -> Adam, cau ASCII -> Lessac.
     */
    fun playMixed(text: String)

    fun stop()
}
