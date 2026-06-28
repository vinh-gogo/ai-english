package com.aienglish.core.common.di

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.security.getPasswordHasher
import org.koin.dsl.module

// Only provides platform-agnostic bindings.
// Platform-specific AppPreferences is provided by androidCoreCommonModule or desktopCoreCommonModule.
val coreCommonModule = module {
    single { AppDispatchers() }
    single { getPasswordHasher() }
}
