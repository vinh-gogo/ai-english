package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateLanguageTargetUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(lang: String) = repository.updateLanguageTarget(lang)
}
