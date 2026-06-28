package com.aienglish.core.common.prefs

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    fun getApiKey(): String
    fun saveApiKey(key: String)
    fun getUserName(): String?
    fun saveUserName(name: String?)
    fun getUserEmail(): String?
    fun saveUserEmail(email: String?)
    fun isSignedIn(): Boolean
    fun setSignedIn(value: Boolean)
    fun getLanguageNative(): String
    fun saveLanguageNative(lang: String)
    fun getLanguageTarget(): String
    fun saveLanguageTarget(lang: String)
    fun isPaid(): Boolean
    fun setPaid(value: Boolean)
    fun clear()

    // Reactive preference streams
    val isPaidFlow: Flow<Boolean>
    val languageNativeFlow: Flow<String>
    val languageTargetFlow: Flow<String>
    val userNameFlow: Flow<String?>
    val userEmailFlow: Flow<String?>
    val isSignedInFlow: Flow<Boolean>
}
