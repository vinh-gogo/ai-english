package com.aienglish.feature.pro.domain.repository

interface ProRepository {
    fun isPaid(): Boolean
    fun savePaid(value: Boolean)
    fun getLanguageNative(): String
}
