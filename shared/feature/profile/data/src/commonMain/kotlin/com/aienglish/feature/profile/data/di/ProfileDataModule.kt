package com.aienglish.feature.profile.data.di

import com.aienglish.feature.profile.data.repository.ProfileRepositoryImpl
import com.aienglish.feature.profile.domain.repository.ProfileRepository
import org.koin.dsl.module

val profileDataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(prefs = get())
    }
}
