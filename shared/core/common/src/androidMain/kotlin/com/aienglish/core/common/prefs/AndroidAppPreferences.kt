package com.aienglish.core.common.prefs

import android.content.Context

class AndroidAppPreferences(context: Context) : AppPreferences {
    private val prefs = context.getSharedPreferences("ai_english_prefs", Context.MODE_PRIVATE)

    override fun getApiKey() = prefs.getString("api_key", "") ?: ""
    override fun saveApiKey(key: String) { prefs.edit().putString("api_key", key).apply() }
    override fun getUserName() = prefs.getString("user_name", null)
    override fun saveUserName(name: String?) { prefs.edit().putString("user_name", name).apply() }
    override fun getUserEmail() = prefs.getString("user_email", null)
    override fun saveUserEmail(email: String?) { prefs.edit().putString("user_email", email).apply() }
    override fun isSignedIn() = prefs.getBoolean("signed_in", false)
    override fun setSignedIn(value: Boolean) { prefs.edit().putBoolean("signed_in", value).apply() }
    override fun clear() { prefs.edit().clear().apply() }
}
