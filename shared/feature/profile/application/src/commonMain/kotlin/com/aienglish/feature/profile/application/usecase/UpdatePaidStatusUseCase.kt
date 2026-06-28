package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdatePaidStatusUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(paid: Boolean) = repository.updatePaidStatus(paid)
}
