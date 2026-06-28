package com.aienglish.feature.aiteacher.data.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.data.repository.TopicRepositoryImpl
import com.aienglish.feature.aiteacher.data.repository.TeacherHistoryRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.history.domain.repository.HistoryRepository
import org.koin.dsl.module

val teacherDataModule = module {
    single<TopicRepository> {
        TopicRepositoryImpl(
            database = get<AIEnglishDatabase>(),
            dispatchers = get<AppDispatchers>()
        )
    }

    single<TeacherHistoryRepository> {
        TeacherHistoryRepositoryImpl(
            historyRepository = get<HistoryRepository>()
        )
    }
}
