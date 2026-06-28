package com.aienglish.core.common.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AndroidAppPreferences
import com.aienglish.core.common.prefs.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidCoreCommonModule = module {
    single<com.aienglish.core.common.security.SecureStorage> { com.aienglish.core.common.security.AndroidSecureStorage(androidContext()) }
    single<AppPreferences> { AndroidAppPreferences(androidContext(), get()) }
}
