package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateAiResponseLengthUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(length: String) = repository.updateAiResponseLength(length)
}
