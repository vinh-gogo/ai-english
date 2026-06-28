package com.aienglish.core.common.security

import java.security.MessageDigest
import java.security.SecureRandom

class DesktopPasswordHasher : PasswordHasher {
    private val random = SecureRandom()

    override fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        // Combine password and salt with a delimiter for absolute security (Pepper/Salted hashing)
        val combined = "$password:$salt".toByteArray(Charsets.UTF_8)
        val hashBytes = md.digest(combined)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    override fun generateSalt(): String {
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }
}

actual fun getPasswordHasher(): PasswordHasher = DesktopPasswordHasher()
