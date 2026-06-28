package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository

class GetNativeLanguageUseCase(
    private val repository: ProRepository
) {
    operator fun invoke(): String = repository.getLanguageNative()
}
