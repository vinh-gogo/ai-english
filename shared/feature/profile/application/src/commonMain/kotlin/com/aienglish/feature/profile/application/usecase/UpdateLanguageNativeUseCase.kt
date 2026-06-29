package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateLanguageNativeUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(lang: String) = repository.updateLanguageNative(lang)
}
