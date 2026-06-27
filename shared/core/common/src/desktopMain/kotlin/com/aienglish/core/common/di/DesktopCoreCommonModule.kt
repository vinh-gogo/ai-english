package com.aienglish.core.common.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.prefs.DesktopAppPreferences
import org.koin.dsl.module

val desktopCoreCommonModule = module {
    single { AppDispatchers() }
    single<AppPreferences> { DesktopAppPreferences() }
}
