package com.aienglish.feature.profile.application.usecase

import com.aienglish.feature.profile.domain.model.UserProfile
import com.aienglish.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UserProfile> = repository.getUserProfile()
}
