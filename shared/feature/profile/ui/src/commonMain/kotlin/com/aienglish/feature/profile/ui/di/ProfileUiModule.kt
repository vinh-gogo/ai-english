package com.aienglish.feature.profile.ui.di

import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import org.koin.dsl.module

val profileUiModule = module {
    single {
        ProfileViewModel(repository = get())
    }
}
