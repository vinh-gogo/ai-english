package com.aienglish.core.common.prefs

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidAppPreferences(
    context: Context,
    private val secureStorage: com.aienglish.core.common.security.SecureStorage
) : AppPreferences {
    private val prefs = context.getSharedPreferences("ai_english_prefs", Context.MODE_PRIVATE)

    private val _isPaid = MutableStateFlow(prefs.getBoolean("is_paid", false))
    override val isPaidFlow = _isPaid.asStateFlow()

    private val _languageNative = MutableStateFlow(prefs.getString("language_native", "vi") ?: "vi")
    override val languageNativeFlow = _languageNative.asStateFlow()

    private val _languageTarget = MutableStateFlow(prefs.getString("language_target", "en") ?: "en")
    override val languageTargetFlow = _languageTarget.asStateFlow()

    private val _userName = MutableStateFlow(prefs.getString("user_name", null))
    override val userNameFlow = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow(prefs.getString("user_email", null))
    override val userEmailFlow = _userEmail.asStateFlow()

    private val _isSignedIn = MutableStateFlow(prefs.getBoolean("signed_in", false))
    override val isSignedInFlow = _isSignedIn.asStateFlow()

    private val _aiResponseLength = MutableStateFlow(prefs.getString("ai_response_length", "normal") ?: "normal")
    override val aiResponseLengthFlow = _aiResponseLength.asStateFlow()

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

    override fun saveUserName(name: String?) {
        prefs.edit().putString("user_name", name).apply()
        _userName.value = name
    }

    override fun getUserEmail() = prefs.getString("user_email", null)

    override fun saveUserEmail(email: String?) {
        prefs.edit().putString("user_email", email).apply()
        _userEmail.value = email
    }

    override fun isSignedIn() = prefs.getBoolean("signed_in", false)

    override fun setSignedIn(value: Boolean) {
        prefs.edit().putBoolean("signed_in", value).apply()
        _isSignedIn.value = value
    }

    override fun getLanguageNative() = prefs.getString("language_native", "vi") ?: "vi"

    override fun saveLanguageNative(lang: String) {
        prefs.edit().putString("language_native", lang).apply()
        _languageNative.value = lang
    }

    override fun getLanguageTarget() = prefs.getString("language_target", "en") ?: "en"

    override fun saveLanguageTarget(lang: String) {
        prefs.edit().putString("language_target", lang).apply()
        _languageTarget.value = lang
    }

    override fun isPaid() = prefs.getBoolean("is_paid", false)

    override fun setPaid(value: Boolean) {
        prefs.edit().putBoolean("is_paid", value).apply()
        _isPaid.value = value
    }

    override fun getAiResponseLength() = prefs.getString("ai_response_length", "normal") ?: "normal"

    override fun saveAiResponseLength(length: String) {
        prefs.edit().putString("ai_response_length", length).apply()
        _aiResponseLength.value = length
    }

    override fun clear() {
        prefs.edit().clear().apply()
        secureStorage.deleteSecret("api_key")
        _userName.value = null
        _userEmail.value = null
        _isSignedIn.value = false
        _languageNative.value = "vi"
        _languageTarget.value = "en"
        _isPaid.value = false
        _aiResponseLength.value = "normal"
    }
}
