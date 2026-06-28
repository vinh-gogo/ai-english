package com.aienglish.core.common.prefs

import android.content.Context

class AndroidAppPreferences(
    context: Context,
    private val secureStorage: com.aienglish.core.common.security.SecureStorage
) : AppPreferences {
    private val prefs = context.getSharedPreferences("ai_english_prefs", Context.MODE_PRIVATE)

    override fun getApiKey(): String {
        val secured = secureStorage.getSecret("api_key")
        if (secured.isNotEmpty()) return secured

        val legacy = prefs.getString("api_key", "") ?: ""
        if (legacy.isNotEmpty()) {
            secureStorage.saveSecret("api_key", legacy)
            prefs.edit().remove("api_key").apply()
            return legacy
        }
        return ""
    }

    override fun saveApiKey(key: String) {
        secureStorage.saveSecret("api_key", key)
    }
    override fun getUserName() = prefs.getString("user_name", null)
    override fun saveUserName(name: String?) { prefs.edit().putString("user_name", name).apply() }
    override fun getUserEmail() = prefs.getString("user_email", null)
    override fun saveUserEmail(email: String?) { prefs.edit().putString("user_email", email).apply() }
    override fun isSignedIn() = prefs.getBoolean("signed_in", false)
    override fun setSignedIn(value: Boolean) { prefs.edit().putBoolean("signed_in", value).apply() }
    override fun getLanguageNative() = prefs.getString("language_native", "vi") ?: "vi"
    override fun saveLanguageNative(lang: String) { prefs.edit().putString("language_native", lang).apply() }
    override fun getLanguageTarget() = prefs.getString("language_target", "en") ?: "en"
    override fun saveLanguageTarget(lang: String) { prefs.edit().putString("language_target", lang).apply() }
    override fun isPaid() = prefs.getBoolean("is_paid", false)
    override fun setPaid(value: Boolean) { prefs.edit().putBoolean("is_paid", value).apply() }
    override fun clear() {
        prefs.edit().clear().apply()
        secureStorage.deleteSecret("api_key")
    }
}
