package com.aienglish.feature.aiteacher.data.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.network.client.GeminiClient
import com.aienglish.feature.aiteacher.data.repository.TeacherRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import org.koin.dsl.module

// TeacherRepository is provided directly in AIEnglishApplication / Main.kt
// where fallbackApiKey is available. This module is intentionally empty.
val teacherDataModule = module {
}
