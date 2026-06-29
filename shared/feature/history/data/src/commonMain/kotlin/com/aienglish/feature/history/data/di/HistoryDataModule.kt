package com.aienglish.feature.history.data.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.history.data.repository.HistoryRepositoryImpl
import com.aienglish.feature.history.domain.repository.HistoryRepository
import org.koin.dsl.module

val historyDataModule = module {
    single<HistoryRepository> {
        HistoryRepositoryImpl(
            database = get<AIEnglishDatabase>(),
            prefs = get<AppPreferences>(),
            dispatchers = get<AppDispatchers>()
        )
    }
}

