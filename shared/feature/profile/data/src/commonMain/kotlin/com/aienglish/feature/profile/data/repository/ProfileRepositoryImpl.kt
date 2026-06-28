package com.aienglish.feature.profile.data.repository

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.profile.domain.model.UserProfile
import com.aienglish.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val prefs: AppPreferences,
    private val dispatchers: AppDispatchers
) : ProfileRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = prefs.getUserName(),
            email = prefs.getUserEmail(),
            isSignedIn = prefs.isSignedIn(),
            languageNative = prefs.getLanguageNative(),
            languageTarget = prefs.getLanguageTarget(),
            isPaid = prefs.isPaid()
        )
    )

    override fun getUserProfile(): Flow<UserProfile> = _userProfile.asStateFlow()

    override suspend fun getApiKey(): String = withContext(dispatchers.io) {
        prefs.getApiKey()
    }

    override suspend fun saveApiKey(key: String) = withContext(dispatchers.io) {
        prefs.saveApiKey(key)
    }

    override suspend fun updateDisplayName(name: String?) = withContext(dispatchers.io) {
        prefs.saveUserName(name)
        _userProfile.update { it.copy(name = name) }
    }

    override suspend fun updateUserEmail(email: String?) = withContext(dispatchers.io) {
        prefs.saveUserEmail(email)
        _userProfile.update { it.copy(email = email) }
    }

    override suspend fun updateSignInStatus(signedIn: Boolean) = withContext(dispatchers.io) {
        prefs.setSignedIn(signedIn)
        _userProfile.update { it.copy(isSignedIn = signedIn) }
    }

    override suspend fun updateLanguageTarget(lang: String) = withContext(dispatchers.io) {
        prefs.saveLanguageTarget(lang)
        _userProfile.update { it.copy(languageTarget = lang) }
    }

    override suspend fun updateLanguageNative(lang: String) = withContext(dispatchers.io) {
        prefs.saveLanguageNative(lang)
        _userProfile.update { it.copy(languageNative = lang) }
    }

    override suspend fun updatePaidStatus(paid: Boolean) = withContext(dispatchers.io) {
        prefs.setPaid(paid)
        _userProfile.update { it.copy(isPaid = paid) }
    }

    override suspend fun clearProfile() = withContext(dispatchers.io) {
        prefs.clear()
        _userProfile.value = UserProfile(
            name = null,
            email = null,
            isSignedIn = false,
            languageNative = "vi",
            languageTarget = "en",
            isPaid = false
        )
    }
}
