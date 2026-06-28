package com.aienglish.feature.pro.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProRepository {
    fun isPaid(): Flow<Boolean>
    suspend fun setPaid(value: Boolean)
    fun getLanguageNative(): Flow<String>
}
