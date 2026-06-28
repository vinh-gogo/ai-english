package com.aienglish.feature.profile.domain.repository

import com.aienglish.feature.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    
    suspend fun getApiKey(): String
    suspend fun saveApiKey(key: String)
    
    suspend fun updateDisplayName(name: String?)
    suspend fun updateUserEmail(email: String?)
    suspend fun updateSignInStatus(signedIn: Boolean)
    suspend fun updateLanguageTarget(lang: String)
    suspend fun updateLanguageNative(lang: String)
    suspend fun updatePaidStatus(paid: Boolean)
    
    suspend fun clearProfile()
}
