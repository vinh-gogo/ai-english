package com.aienglish.feature.pro.data.repository

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.pro.domain.repository.ProRepository

class ProRepositoryImpl(
    private val prefs: AppPreferences
) : ProRepository {
    override fun isPaid(): Boolean = prefs.isPaid()
    override fun savePaid(value: Boolean) = prefs.setPaid(value)
    override fun getLanguageNative(): String = prefs.getLanguageNative()
}
