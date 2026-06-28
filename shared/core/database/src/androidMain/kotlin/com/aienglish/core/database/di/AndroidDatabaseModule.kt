package com.aienglish.core.database.di

import android.content.Context
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.core.database.driver.createAndroidDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val coreDatabaseModule: Module = module {
    single { createAndroidDriver(get<Context>()) }
    single { AIEnglishDatabase(get()) }
}
