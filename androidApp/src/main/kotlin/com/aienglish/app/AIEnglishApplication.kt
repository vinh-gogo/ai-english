package com.aienglish.app

import android.app.Application
import com.aienglish.core.common.di.androidCoreCommonModule
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.core.database.driver.createAndroidDriver
import com.aienglish.core.network.di.coreNetworkModule
import com.aienglish.feature.aiteacher.application.di.teacherApplicationModule
import com.aienglish.feature.aiteacher.data.di.teacherDataModule
import com.aienglish.feature.aiteacher.data.repository.TeacherRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import com.aienglish.feature.aiteacher.ui.di.teacherUiModule
import com.aienglish.feature.profile.data.di.profileDataModule
import com.aienglish.feature.profile.ui.di.profileUiModule
import com.aienglish.feature.history.data.di.historyDataModule
import com.aienglish.feature.history.ui.di.historyUiModule
import com.aienglish.feature.pro.data.di.proDataModule
import com.aienglish.feature.pro.ui.di.proUiModule
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import com.aienglish.app.resource.AndroidJsonResourceReader
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AIEnglishApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val buildConfigApiKey = BuildConfig.GEMINI_API_KEY

        startKoin {
            androidContext(this@AIEnglishApplication)
            modules(
                androidCoreCommonModule,   // provides AndroidAppPreferences + AppDispatchers
                coreNetworkModule,
                teacherApplicationModule,
                // Database module
                module {
                    single { createAndroidDriver(androidContext()) }
                    single { AIEnglishDatabase(get()) }
                },
                // Data module (HistoryRepository)
                teacherDataModule,
                profileDataModule,
                historyDataModule,
                proDataModule,
                // TeacherRepository (needs API key)
                module {
                    single<TeacherRepository> {
                        TeacherRepositoryImpl(
                            geminiClient = get(),
                            dispatchers = get(),
                            prefs = get<AppPreferences>(),
                            fallbackApiKey = buildConfigApiKey
                        )
                    }
                    single<JsonResourceReader> { AndroidJsonResourceReader(androidContext()) }
                },
                teacherUiModule,
                profileUiModule,
                historyUiModule,
                proUiModule
            )
        }
    }
}
