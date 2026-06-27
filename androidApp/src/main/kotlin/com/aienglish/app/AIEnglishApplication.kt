package com.aienglish.app

import android.app.Application
import com.aienglish.core.common.di.androidCoreCommonModule
import com.aienglish.core.network.di.coreNetworkModule
import com.aienglish.feature.aiteacher.application.di.teacherApplicationModule
import com.aienglish.feature.aiteacher.data.di.teacherDataModule
import com.aienglish.feature.aiteacher.data.repository.TeacherRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import com.aienglish.feature.aiteacher.ui.di.teacherUiModule
import com.aienglish.core.common.prefs.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AIEnglishApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // API key from BuildConfig (can be overridden by user in ProfileSheet)
        val buildConfigApiKey = BuildConfig.GEMINI_API_KEY

        startKoin {
            androidContext(this@AIEnglishApplication)
            modules(
                androidCoreCommonModule,   // provides AndroidAppPreferences + AppDispatchers
                coreNetworkModule,
                teacherApplicationModule,
                module {
                    single<TeacherRepository> {
                        val prefs = get<AppPreferences>()
                        TeacherRepositoryImpl(
                            geminiClient = get(),
                            dispatchers = get(),
                            prefs = prefs,
                            fallbackApiKey = buildConfigApiKey
                        )
                    }
                },
                teacherUiModule
            )
        }
    }
}
