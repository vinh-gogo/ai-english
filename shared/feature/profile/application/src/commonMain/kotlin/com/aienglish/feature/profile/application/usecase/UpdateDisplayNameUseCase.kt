package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateDisplayNameUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(name: String?) = repository.updateDisplayName(name)
}
