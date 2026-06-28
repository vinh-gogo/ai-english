package com.aienglish.core.common.security

interface SecureStorage {
    fun saveSecret(key: String, value: String)
    fun getSecret(key: String): String
    fun deleteSecret(key: String)
}
