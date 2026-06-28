package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class ClearProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() = repository.clearProfile()
}
