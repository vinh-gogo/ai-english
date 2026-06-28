package com.aienglish.feature.profile.data.repository

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.profile.domain.model.UserProfile
import com.aienglish.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val prefs: AppPreferences,
    private val dispatchers: AppDispatchers
) : ProfileRepository {

    override fun getUserProfile(): Flow<UserProfile> = combine<Any?, UserProfile>(
        prefs.userNameFlow,
        prefs.userEmailFlow,
        prefs.isSignedInFlow,
        prefs.languageNativeFlow,
        prefs.languageTargetFlow,
        prefs.isPaidFlow
    ) { array ->
        UserProfile(
            name = array[0] as String?,
            email = array[1] as String?,
            isSignedIn = array[2] as Boolean,
            languageNative = array[3] as String,
            languageTarget = array[4] as String,
            isPaid = array[5] as Boolean
        )
    }

    override suspend fun getApiKey(): String = withContext(dispatchers.io) {
        prefs.getApiKey()
    }

    override suspend fun saveApiKey(key: String) = withContext(dispatchers.io) {
        prefs.saveApiKey(key)
    }

    override suspend fun updateDisplayName(name: String?) = withContext(dispatchers.io) {
        prefs.saveUserName(name)
    }

    override suspend fun updateUserEmail(email: String?) = withContext(dispatchers.io) {
        prefs.saveUserEmail(email)
    }

    override suspend fun updateSignInStatus(signedIn: Boolean) = withContext(dispatchers.io) {
        prefs.setSignedIn(signedIn)
    }

    override suspend fun updateLanguageTarget(lang: String) = withContext(dispatchers.io) {
        prefs.saveLanguageTarget(lang)
    }

    override suspend fun updateLanguageNative(lang: String) = withContext(dispatchers.io) {
        prefs.saveLanguageNative(lang)
    }

    override suspend fun updatePaidStatus(paid: Boolean) = withContext(dispatchers.io) {
        prefs.setPaid(paid)
    }

    override suspend fun clearProfile() = withContext(dispatchers.io) {
        prefs.clear()
    }
}
