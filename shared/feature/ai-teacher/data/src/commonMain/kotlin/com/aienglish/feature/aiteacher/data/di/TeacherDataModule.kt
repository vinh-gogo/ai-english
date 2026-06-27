package com.aienglish.feature.aiteacher.data.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.data.repository.HistoryRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.HistoryRepository
import org.koin.dsl.module

// TeacherRepository is registered in platform entry points (needs fallbackApiKey)
val teacherDataModule = module {
    single<HistoryRepository> {
        HistoryRepositoryImpl(
            database = get<AIEnglishDatabase>(),
            dispatchers = get<AppDispatchers>()
        )
    }
}
