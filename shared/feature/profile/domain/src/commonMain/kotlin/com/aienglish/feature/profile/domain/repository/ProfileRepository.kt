package com.aienglish.feature.profile.domain.repository

interface ProfileRepository {
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
    fun savePaid(value: Boolean)
}
