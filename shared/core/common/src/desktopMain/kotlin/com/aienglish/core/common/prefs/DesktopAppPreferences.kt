package com.aienglish.core.common.prefs

import java.util.prefs.Preferences

class DesktopAppPreferences : AppPreferences {
    private val prefs = Preferences.userRoot().node("ai_english")

    override fun getApiKey() = prefs.get("api_key", "")
    override fun saveApiKey(key: String) { prefs.put("api_key", key) }
    override fun getUserName() = prefs.get("user_name", null).takeIf { !it.isNullOrEmpty() }
    override fun saveUserName(name: String?) { if (name != null) prefs.put("user_name", name) else prefs.remove("user_name") }
    override fun getUserEmail() = prefs.get("user_email", null).takeIf { !it.isNullOrEmpty() }
    override fun saveUserEmail(email: String?) { if (email != null) prefs.put("user_email", email) else prefs.remove("user_email") }
    override fun isSignedIn() = prefs.getBoolean("signed_in", false)
    override fun setSignedIn(value: Boolean) { prefs.putBoolean("signed_in", value) }
    override fun clear() { prefs.clear() }
}
