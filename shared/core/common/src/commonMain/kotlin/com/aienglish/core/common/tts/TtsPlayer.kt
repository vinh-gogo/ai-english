package com.aienglish.core.common.tts

interface TtsPlayer {
    fun play(text: String, languageCode: String)
    fun stop()
}
