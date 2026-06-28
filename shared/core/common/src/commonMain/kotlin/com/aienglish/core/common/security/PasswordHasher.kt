package com.aienglish.core.common.security

interface PasswordHasher {
    /**
     * Hashes the password securely with the given salt using SHA-256.
     */
    fun hashPassword(password: String, salt: String): String

    /**
     * Generates a cryptographically secure random salt (HEX encoded).
     */
    fun generateSalt(): String
}

expect fun getPasswordHasher(): PasswordHasher
