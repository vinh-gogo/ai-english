package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository

class SetVipStatusUseCase(
    private val repository: ProRepository
) {
    operator fun invoke(isPaid: Boolean) = repository.savePaid(isPaid)
}
