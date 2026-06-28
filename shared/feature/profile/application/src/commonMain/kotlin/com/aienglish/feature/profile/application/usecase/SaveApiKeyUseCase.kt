package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class SaveApiKeyUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(key: String) = repository.saveApiKey(key)
}
