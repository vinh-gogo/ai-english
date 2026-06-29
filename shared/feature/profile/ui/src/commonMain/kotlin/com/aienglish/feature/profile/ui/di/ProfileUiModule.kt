package com.aienglish.feature.profile.ui.di

import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import org.koin.dsl.module

val profileUiModule = module {
    single {
        ProfileViewModel(
            getUserProfileUseCase = get(),
            getApiKeyUseCase = get(),
            saveApiKeyUseCase = get(),
            updateLanguageTargetUseCase = get(),
            updateLanguageNativeUseCase = get(),
            updateDisplayNameUseCase = get(),
            updateUserEmailUseCase = get(),
            updateSignInStatusUseCase = get(),
            clearProfileUseCase = get(),
            updatePaidStatusUseCase = get(),
            updateAiResponseLengthUseCase = get(),
            getNovitaApiKeyUseCase = get(),
            saveNovitaApiKeyUseCase = get(),
            updateApiProviderUseCase = get()
        )
    }
}
