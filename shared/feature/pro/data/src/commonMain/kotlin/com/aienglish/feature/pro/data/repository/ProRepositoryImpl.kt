package com.aienglish.feature.pro.data.repository

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.pro.domain.repository.ProRepository
import kotlinx.coroutines.flow.Flow

class ProRepositoryImpl(
    private val prefs: AppPreferences
) : ProRepository {
    override fun isPaid(): Flow<Boolean> = prefs.isPaidFlow
    override suspend fun setPaid(value: Boolean) {
        prefs.setPaid(value)
    }
    override fun getLanguageNative(): Flow<String> = prefs.languageNativeFlow
}
