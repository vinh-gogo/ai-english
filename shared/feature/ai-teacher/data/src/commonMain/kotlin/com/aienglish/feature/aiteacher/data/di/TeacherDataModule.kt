package com.aienglish.feature.aiteacher.data.di

import org.koin.dsl.module

// TeacherRepository is registered in platform entry points (needs fallbackApiKey)
val teacherDataModule = module {
    // Empty (HistoryRepository moved to shared:feature:history:data)
}
