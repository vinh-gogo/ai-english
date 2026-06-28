package com.aienglish.feature.profile.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.profile.domain.repository.ProfileRepository

data class ProfileState(
    val showProfile: Boolean = false,
    val isSignedIn: Boolean = false,
    val displayName: String? = null,
    val email: String? = null,
    val apiKeyInput: String = "",
    val apiKeyVisible: Boolean = false,
    val apiKeySaved: Boolean = false,
    val howToExpanded: Boolean = false,
    val isSigningIn: Boolean = false,
    val languageNative: String = "vi",
    val languageTarget: String = "en",
    val monthlyUsage: Int = 0,
    val monthlyLimit: Int = 5,
    val isPaid: Boolean = false,
    val showVip: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val repository: ProfileRepository
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        // Load persisted data on start
        setState {
            copy(
                isSignedIn = repository.isSignedIn(),
                displayName = repository.getUserName(),
                email = repository.getUserEmail(),
                apiKeyInput = repository.getApiKey(),
                languageNative = repository.getLanguageNative(),
                languageTarget = repository.getLanguageTarget(),
                isPaid = repository.isPaid()
            )
        }
    }

    fun updateMonthlyUsage(usage: Int, limit: Int) {
        setState { copy(monthlyUsage = usage, monthlyLimit = limit) }
    }

    fun setPaid(value: Boolean) {
        repository.savePaid(value)
        setState { copy(isPaid = value) }
    }

    fun showProfile() = setState { copy(showProfile = true) }
    fun hideProfile() = setState { copy(showProfile = false, error = null) }
    fun showVip() = setState { copy(showVip = true) }
    fun hideVip() = setState { copy(showVip = false) }

    fun onApiKeyChanged(key: String) = setState { copy(apiKeyInput = key, apiKeySaved = false) }
    fun toggleApiKeyVisibility() = setState { copy(apiKeyVisible = !apiKeyVisible) }

    fun saveApiKey() {
        val key = currentState.apiKeyInput.trim()
        if (key.isEmpty()) { setState { copy(error = "API Key không được để trống") }; return }
        repository.saveApiKey(key)
        setState { copy(apiKeySaved = true, error = null) }
    }

    fun toggleHowTo() = setState { copy(howToExpanded = !howToExpanded) }

    fun onLanguageTargetChanged(lang: String) {
        repository.saveLanguageTarget(lang)
        setState { copy(languageTarget = lang) }
    }

    // Called after successful Google Sign-In
    fun onSignedIn(name: String, email: String) {
        repository.saveUserName(name)
        repository.saveUserEmail(email)
        repository.setSignedIn(true)
        setState { copy(isSignedIn = true, displayName = name, email = email, isSigningIn = false, error = null) }
    }

    fun signInAsGuest() {
        repository.saveUserName("Khách")
        repository.saveUserEmail("guest@aienglish.com")
        repository.setSignedIn(true)
        setState { copy(isSignedIn = true, displayName = "Khách", email = "guest@aienglish.com", isSigningIn = false, error = null) }
    }

    fun onSigningIn() = setState { copy(isSigningIn = true, error = null) }
    fun onSignInError(msg: String) = setState { copy(isSigningIn = false, error = msg) }

    fun signOut() {
        repository.setSignedIn(false)
        repository.saveUserName(null)
        repository.saveUserEmail(null)
        setState { copy(isSignedIn = false, displayName = null, email = null, error = null) }
    }

    fun dismissError() = setState { copy(error = null) }
}
