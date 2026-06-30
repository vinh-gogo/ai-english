package com.aienglish.core.common.prefs

import java.util.prefs.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DesktopAppPreferences(
    private val secureStorage: com.aienglish.core.common.security.SecureStorage
) : AppPreferences {
    private val prefs = Preferences.userRoot().node("ai_english")

    private val _isPaid = MutableStateFlow(prefs.getBoolean("is_paid", false))
    override val isPaidFlow = _isPaid.asStateFlow()

    private val _languageNative = MutableStateFlow(prefs.get("language_native", "vi"))
    override val languageNativeFlow = _languageNative.asStateFlow()

    private val _languageTarget = MutableStateFlow(prefs.get("language_target", "en"))
    override val languageTargetFlow = _languageTarget.asStateFlow()

    private val _userName = MutableStateFlow(prefs.get("user_name", null).takeIf { !it.isNullOrEmpty() })
    override val userNameFlow = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow(prefs.get("user_email", null).takeIf { !it.isNullOrEmpty() })
    override val userEmailFlow = _userEmail.asStateFlow()

    private val _isSignedIn = MutableStateFlow(prefs.getBoolean("signed_in", false))
    override val isSignedInFlow = _isSignedIn.asStateFlow()

    private val _aiResponseLength = MutableStateFlow(prefs.get("ai_response_length", "normal"))
    override val aiResponseLengthFlow = _aiResponseLength.asStateFlow()

    private val _apiProvider = MutableStateFlow(prefs.get("api_provider", "gemini"))
    override val apiProviderFlow = _apiProvider.asStateFlow()

    private val _novitaApiKey = MutableStateFlow(secureStorage.getSecret("novita_api_key"))
    override val novitaApiKeyFlow = _novitaApiKey.asStateFlow()

    private val _appTheme = MutableStateFlow(prefs.get("app_theme", "SUNSET_WARMTH"))
    override val appThemeFlow = _appTheme.asStateFlow()

    private val _ttsSpeed = MutableStateFlow(prefs.get("tts_speed", "slow"))
    override val ttsSpeedFlow = _ttsSpeed.asStateFlow()

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

    override fun getApiProvider() = prefs.get("api_provider", "gemini")

    override fun saveApiProvider(provider: String) {
        prefs.put("api_provider", provider)
        _apiProvider.value = provider
    }

    override fun getNovitaApiKey(): String {
        val secured = secureStorage.getSecret("novita_api_key")
        if (secured.isNotEmpty()) return secured

        val legacy = prefs.get("novita_api_key", "")
        if (legacy.isNotEmpty()) {
            secureStorage.saveSecret("novita_api_key", legacy)
            prefs.remove("novita_api_key")
            _novitaApiKey.value = legacy
            return legacy
        }
        return ""
    }

    override fun saveNovitaApiKey(key: String) {
        secureStorage.saveSecret("novita_api_key", key)
        _novitaApiKey.value = key
    }

    override fun getUserName() = prefs.get("user_name", null).takeIf { !it.isNullOrEmpty() }
    
    override fun saveUserName(name: String?) {
        if (name != null) prefs.put("user_name", name) else prefs.remove("user_name")
        _userName.value = name
    }

    override fun getUserEmail() = prefs.get("user_email", null).takeIf { !it.isNullOrEmpty() }

    override fun saveUserEmail(email: String?) {
        if (email != null) prefs.put("user_email", email) else prefs.remove("user_email")
        _userEmail.value = email
    }

    override fun isSignedIn() = prefs.getBoolean("signed_in", false)

    override fun setSignedIn(value: Boolean) {
        prefs.putBoolean("signed_in", value)
        _isSignedIn.value = value
    }

    override fun getLanguageNative() = prefs.get("language_native", "vi")

    override fun saveLanguageNative(lang: String) {
        prefs.put("language_native", lang)
        _languageNative.value = lang
    }

    override fun getLanguageTarget() = prefs.get("language_target", "en")

    override fun saveLanguageTarget(lang: String) {
        prefs.put("language_target", lang)
        _languageTarget.value = lang
    }

    override fun isPaid() = prefs.getBoolean("is_paid", false)

    override fun setPaid(value: Boolean) {
        prefs.putBoolean("is_paid", value)
        _isPaid.value = value
    }

    override fun getAiResponseLength() = prefs.get("ai_response_length", "normal")

    override fun saveAiResponseLength(length: String) {
        prefs.put("ai_response_length", length)
        _aiResponseLength.value = length
    }

    override fun getAppTheme() = prefs.get("app_theme", "SUNSET_WARMTH")

    override fun saveAppTheme(theme: String) {
        prefs.put("app_theme", theme)
        _appTheme.value = theme
    }

    override fun getTtsSpeed() = prefs.get("tts_speed", "slow")

    override fun saveTtsSpeed(speed: String) {
        prefs.put("tts_speed", speed)
        _ttsSpeed.value = speed
    }

    override fun clear() {
        prefs.clear()
        secureStorage.deleteSecret("api_key")
        secureStorage.deleteSecret("novita_api_key")
        _userName.value = null
        _userEmail.value = null
        _isSignedIn.value = false
        _languageNative.value = "vi"
        _languageTarget.value = "en"
        _isPaid.value = false
        _aiResponseLength.value = "normal"
        _apiProvider.value = "gemini"
        _novitaApiKey.value = ""
        _ttsSpeed.value = "slow"
    }
}
