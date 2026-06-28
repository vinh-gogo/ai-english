package com.aienglish.core.database.di

import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.core.database.driver.createDesktopDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val coreDatabaseModule: Module = module {
    single { createDesktopDriver() }
    single { AIEnglishDatabase(get()) }
}
