package com.aienglish.feature.profile.data.repository

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val prefs: AppPreferences
) : ProfileRepository {
    override fun getApiKey(): String = prefs.getApiKey()
    override fun saveApiKey(key: String) = prefs.saveApiKey(key)
    override fun getUserName(): String? = prefs.getUserName()
    override fun saveUserName(name: String?) = prefs.saveUserName(name)
    override fun getUserEmail(): String? = prefs.getUserEmail()
    override fun saveUserEmail(email: String?) = prefs.saveUserEmail(email)
    override fun isSignedIn(): Boolean = prefs.isSignedIn()
    override fun setSignedIn(value: Boolean) = prefs.setSignedIn(value)
}
