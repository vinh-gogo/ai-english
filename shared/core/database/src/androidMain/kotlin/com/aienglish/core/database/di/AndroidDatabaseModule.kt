package com.aienglish.core.database.di

import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.core.database.driver.createAndroidDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val coreDatabaseModule: Module = module {
    single { createAndroidDriver(androidContext()) }
    single { AIEnglishDatabase(get()) }
}
