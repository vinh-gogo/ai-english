package com.aienglish.core.common.security

import java.io.BufferedReader
import java.io.InputStreamReader

class DesktopSecureStorage : SecureStorage {
    private val osName = System.getProperty("os.name").lowercase()

    override fun saveSecret(key: String, value: String) {
        when {
            osName.contains("mac") -> saveMacKeychain(key, value)
            osName.contains("win") -> saveWindowsDpapi(key, value)
            else -> saveFallback(key, value)
        }
    }

    override fun getSecret(key: String): String {
        return when {
            osName.contains("mac") -> getMacKeychain(key)
            osName.contains("win") -> getWindowsDpapi(key)
            else -> getFallback(key)
        }
    }

    override fun deleteSecret(key: String) {
        when {
            osName.contains("mac") -> deleteMacKeychain(key)
            osName.contains("win") -> deleteWindowsDpapi(key)
            else -> deleteFallback(key)
        }
    }

    // ── macOS Keychain CLI Integration ──
    private fun saveMacKeychain(key: String, value: String) {
        try {
            ProcessBuilder("security", "add-generic-password", "-a", "AIEnglishUser", "-s", key, "-w", value, "-U").start().waitFor()
        } catch (e: Exception) {
            saveFallback(key, value)
        }
    }

    private fun getMacKeychain(key: String): String {
        return try {
            val process = ProcessBuilder("security", "find-generic-password", "-a", "AIEnglishUser", "-s", key, "-w").start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = reader.readLine()?.trim() ?: ""
            process.waitFor()
            result
        } catch (e: Exception) {
            getFallback(key)
        }
    }

    private fun deleteMacKeychain(key: String) {
        try {
            ProcessBuilder("security", "delete-generic-password", "-a", "AIEnglishUser", "-s", key).start().waitFor()
        } catch (e: Exception) {
            deleteFallback(key)
        }
    }

    // ── Windows DPAPI via PowerShell Integration ──
    private fun saveWindowsDpapi(key: String, value: String) {
        try {
            val command = "Add-Type -AssemblyName System.Security; " +
                    "\$bytes = [System.Text.Encoding]::UTF8.GetBytes('$value'); " +
                    "\$protected = [System.Security.Cryptography.ProtectedData]::Protect(\$bytes, \$null, 'CurrentUser'); " +
                    "[System.Convert]::ToBase64String(\$protected)"
            
            val process = ProcessBuilder("powershell", "-Command", command).start()
            val base64Encrypted = BufferedReader(InputStreamReader(process.inputStream)).readLine()?.trim() ?: ""
            process.waitFor()
            if (base64Encrypted.isNotEmpty()) {
                saveFallback(key, base64Encrypted)
            }
        } catch (e: Exception) {
            saveFallback(key, value)
        }
    }

    private fun getWindowsDpapi(key: String): String {
        val encryptedBase64 = getFallback(key)
        if (encryptedBase64.isEmpty()) return ""
        return try {
            val command = "Add-Type -AssemblyName System.Security; " +
                    "\$bytes = [System.Convert]::FromBase64String('$encryptedBase64'); " +
                    "\$decrypted = [System.Security.Cryptography.ProtectedData]::Unprotect(\$bytes, \$null, 'CurrentUser'); " +
                    "[System.Text.Encoding]::UTF8.GetString(\$decrypted)"
            
            val process = ProcessBuilder("powershell", "-Command", command).start()
            val decrypted = BufferedReader(InputStreamReader(process.inputStream)).readLine()?.trim() ?: ""
            process.waitFor()
            decrypted
        } catch (e: Exception) {
            ""
        }
    }

    private fun deleteWindowsDpapi(key: String) {
        deleteFallback(key)
    }

    // ── Fallback (Preferences thường) ──
    private val prefs = java.util.prefs.Preferences.userRoot().node("ai_english_fallback")
    private fun saveFallback(key: String, value: String) = prefs.put(key, value)
    private fun getFallback(key: String): String = prefs.get(key, "")
    private fun deleteFallback(key: String) = prefs.remove(key)
}
