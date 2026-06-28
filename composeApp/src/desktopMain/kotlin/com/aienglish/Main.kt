package com.aienglish

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.aienglish.core.designsystem.theme.AIEnglishTheme
import com.aienglish.core.network.di.coreNetworkModule
import com.aienglish.core.common.di.coreCommonModule
import com.aienglish.core.common.di.desktopCoreCommonModule
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.database.di.coreDatabaseModule
import com.aienglish.feature.aiteacher.application.di.teacherApplicationModule
import com.aienglish.feature.aiteacher.data.di.teacherDataModule
import com.aienglish.feature.aiteacher.data.repository.TeacherRepositoryImpl
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import com.aienglish.feature.aiteacher.ui.di.teacherUiModule
import com.aienglish.feature.aiteacher.ui.screen.TeacherScreen
import com.aienglish.feature.profile.data.di.profileDataModule
import com.aienglish.feature.profile.ui.di.profileUiModule
import com.aienglish.feature.history.data.di.historyDataModule
import com.aienglish.feature.history.ui.di.historyUiModule
import com.aienglish.feature.pro.data.di.proDataModule
import com.aienglish.feature.pro.ui.di.proUiModule
import com.aienglish.feature.pro.application.di.proApplicationModule
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import com.aienglish.resource.DesktopJsonResourceReader
import org.koin.core.context.startKoin
import org.koin.dsl.module

// ── Load API key from local config ──
private fun loadApiKey(): String {
    val props = java.util.Properties()
    val file = java.io.File("local.properties")
    if (file.exists()) {
        props.load(file.inputStream())
        return props.getProperty("GEMINI_API_KEY", "")
    }
    return System.getenv("GEMINI_API_KEY") ?: ""
}

fun main() {
    val apiKey = loadApiKey()

    startKoin {
        modules(
            coreCommonModule,
            desktopCoreCommonModule,   // provides DesktopAppPreferences + secure storage
            coreDatabaseModule,
            coreNetworkModule,
            teacherApplicationModule,
            // Data module (HistoryRepository)
            teacherDataModule,
            profileDataModule,
            historyDataModule,
            proDataModule,
            proApplicationModule,
            module {
                single<TeacherRepository> {
                    val prefs = get<AppPreferences>()
                    val geminiClient = get<com.aienglish.core.network.client.GeminiClient>()
                    val dispatchers = get<com.aienglish.core.common.dispatchers.AppDispatchers>()
                    TeacherRepositoryImpl(
                        geminiClient = geminiClient,
                        dispatchers = dispatchers,
                        prefs = prefs,
                        fallbackApiKey = apiKey
                    )
                }
                single<JsonResourceReader> { DesktopJsonResourceReader() }
            },
            teacherUiModule,
            profileUiModule,
            historyUiModule,
            proUiModule
        )
    }

    application {
        val windowState = rememberWindowState(width = 400.dp, height = 800.dp)
        Window(
            onCloseRequest = ::exitApplication,
            title = "AI English Teacher",
            state = windowState
        ) {
            AIEnglishTheme {
                TeacherScreen()
            }
        }
    }
}
