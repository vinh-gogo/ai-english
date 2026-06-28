package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository

class SetVipStatusUseCase(
    private val repository: ProRepository
) {
    suspend operator fun invoke(value: Boolean) = repository.setPaid(value)
}
