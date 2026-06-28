package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository
import kotlinx.coroutines.flow.Flow

class GetNativeLanguageUseCase(
    private val repository: ProRepository
) {
    operator fun invoke(): Flow<String> = repository.getLanguageNative()
}
