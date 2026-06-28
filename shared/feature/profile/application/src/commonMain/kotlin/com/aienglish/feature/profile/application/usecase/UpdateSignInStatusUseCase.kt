package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.repository.ProfileRepository

class UpdateSignInStatusUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(signedIn: Boolean) = repository.updateSignInStatus(signedIn)
}
