package com.aienglish.core.common.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class AndroidSecureStorage(context: Context) : SecureStorage {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPrefs = EncryptedSharedPreferences.create(
        "ai_english_secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveSecret(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    override fun getSecret(key: String): String {
        return sharedPrefs.getString(key, "") ?: ""
    }

    override fun deleteSecret(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }
}
