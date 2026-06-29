package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateApiProviderUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(provider: String) = repository.updateApiProvider(provider)
}
