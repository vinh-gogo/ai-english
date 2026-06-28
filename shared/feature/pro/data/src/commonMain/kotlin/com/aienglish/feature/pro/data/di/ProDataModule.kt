package com.aienglish.feature.pro.data.di

import com.aienglish.feature.pro.data.repository.ProRepositoryImpl
import com.aienglish.feature.pro.domain.repository.ProRepository
import org.koin.dsl.module

val proDataModule = module {
    single<ProRepository> { ProRepositoryImpl(get()) }
}
