package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository

class GetVipStatusUseCase(
    private val repository: ProRepository
) {
    operator fun invoke(): Boolean = repository.isPaid()
}
