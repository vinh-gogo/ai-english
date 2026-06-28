package com.aienglish.feature.pro.application.usecase

import com.aienglish.feature.pro.domain.repository.ProRepository
import kotlinx.coroutines.flow.Flow

class GetVipStatusUseCase(
    private val repository: ProRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isPaid()
}
