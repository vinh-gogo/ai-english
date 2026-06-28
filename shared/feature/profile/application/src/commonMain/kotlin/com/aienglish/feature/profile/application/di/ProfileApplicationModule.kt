package com.aienglish.feature.profile.application.di

import com.aienglish.feature.profile.application.usecase.*
import org.koin.dsl.module

val profileApplicationModule = module {
    factory { GetUserProfileUseCase(get()) }
    factory { GetApiKeyUseCase(get()) }
    factory { SaveApiKeyUseCase(get()) }
    factory { UpdateLanguageTargetUseCase(get()) }
    factory { UpdateDisplayNameUseCase(get()) }
    factory { UpdateUserEmailUseCase(get()) }
    factory { UpdateSignInStatusUseCase(get()) }
    factory { ClearProfileUseCase(get()) }
    factory { UpdatePaidStatusUseCase(get()) }
    factory { UpdateAiResponseLengthUseCase(get()) }
}
