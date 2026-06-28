package com.aienglish.core.common.prefs

import java.util.prefs.Preferences

class DesktopAppPreferences(
    private val secureStorage: com.aienglish.core.common.security.SecureStorage
) : AppPreferences {
    private val prefs = Preferences.userRoot().node("ai_english")

    override fun getApiKey(): String {
        val secured = secureStorage.getSecret("api_key")
        if (secured.isNotEmpty()) return secured

        val legacy = prefs.get("api_key", "")
        if (legacy.isNotEmpty()) {
            secureStorage.saveSecret("api_key", legacy)
            prefs.remove("api_key")
            return legacy
        }
        return ""
    }

    override fun saveApiKey(key: String) {
        secureStorage.saveSecret("api_key", key)
    }
    override fun getUserName() = prefs.get("user_name", null).takeIf { !it.isNullOrEmpty() }
    override fun saveUserName(name: String?) { if (name != null) prefs.put("user_name", name) else prefs.remove("user_name") }
    override fun getUserEmail() = prefs.get("user_email", null).takeIf { !it.isNullOrEmpty() }
    override fun saveUserEmail(email: String?) { if (email != null) prefs.put("user_email", email) else prefs.remove("user_email") }
    override fun isSignedIn() = prefs.getBoolean("signed_in", false)
    override fun setSignedIn(value: Boolean) { prefs.putBoolean("signed_in", value) }
    override fun getLanguageNative() = prefs.get("language_native", "vi")
    override fun saveLanguageNative(lang: String) { prefs.put("language_native", lang) }
    override fun getLanguageTarget() = prefs.get("language_target", "en")
    override fun saveLanguageTarget(lang: String) { prefs.put("language_target", lang) }
    override fun isPaid() = prefs.getBoolean("is_paid", false)
    override fun setPaid(value: Boolean) { prefs.putBoolean("is_paid", value) }
    override fun clear() {
        prefs.clear()
        secureStorage.deleteSecret("api_key")
    }
}
