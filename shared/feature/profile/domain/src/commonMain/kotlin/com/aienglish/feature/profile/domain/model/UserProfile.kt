package com.aienglish.feature.profile.domain.model

data class UserProfile(
    val name: String?,
    val email: String?,
    val isSignedIn: Boolean,
    val languageNative: String,
    val languageTarget: String,
    val isPaid: Boolean,
    val aiResponseLength: String,
    val apiProvider: String = "gemini"
)
