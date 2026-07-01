package com.aienglish.feature.aiteacher.data.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.data.repository.TopicRepositoryImpl
import com.aienglish.feature.aiteacher.data.repository.TeacherHistoryRepositoryImpl
import com.aienglish.feature.aiteacher.data.repository.TeacherRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val teacherDataModule = module {
    single {
        val driver = get<app.cash.sqldelight.db.SqlDriver>()
        try {
            com.aienglish.feature.aiteacher.data.database.AiTeacherDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // Ignored if already created
        }
        com.aienglish.feature.aiteacher.data.database.AiTeacherDatabase(driver)
    }

    single<TopicRepository> {
        TopicRepositoryImpl(
            coreDatabase = get(),
            aiTeacherDatabase = get(),
            prefs = get(),
            dispatchers = get(),
            jsonResourceReader = get()
        )
    }

    single<TeacherHistoryRepository> {
        TeacherHistoryRepositoryImpl(
            historyRepository = get(),
            database = get(),
            dispatchers = get()
        )
    }

    single<TeacherRepository> {
        TeacherRepositoryImpl(
            geminiClient = get(),
            novitaClient = get(),
            dispatchers = get(),
            prefs = get(),
            fallbackApiKey = get(named("fallback_api_key"))
        )
    }
}
