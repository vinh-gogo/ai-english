package com.aienglish.feature.profile.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.launch

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
    val error: String? = null
)

class ProfileViewModel(
    private val repository: ProfileRepository
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        // Collect reactive user profile
        viewModelScope.launch {
            repository.getUserProfile().collect { profile ->
                setState {
                    copy(
                        isSignedIn = profile.isSignedIn,
                        displayName = profile.name,
                        email = profile.email,
                        languageNative = profile.languageNative,
                        languageTarget = profile.languageTarget,
                        isPaid = profile.isPaid
                    )
                }
            }
        }
        
        // Load API key async
        viewModelScope.launch {
            val key = repository.getApiKey()
            setState { copy(apiKeyInput = key) }
        }
    }

    fun updateMonthlyUsage(usage: Int, limit: Int) {
        setState { copy(monthlyUsage = usage, monthlyLimit = limit) }
    }

    fun setPaid(value: Boolean) {
        viewModelScope.launch {
            repository.updatePaidStatus(value)
        }
    }

    fun showProfile() = setState { copy(showProfile = true) }
    fun hideProfile() = setState { copy(showProfile = false, error = null) }

    fun onApiKeyChanged(key: String) = setState { copy(apiKeyInput = key, apiKeySaved = false) }
    fun toggleApiKeyVisibility() = setState { copy(apiKeyVisible = !apiKeyVisible) }

    fun saveApiKey() {
        val key = currentState.apiKeyInput.trim()
        if (key.isEmpty()) { setState { copy(error = "API Key không được để trống") }; return }
        viewModelScope.launch {
            repository.saveApiKey(key)
            setState { copy(apiKeySaved = true, error = null) }
        }
    }

    fun toggleHowTo() = setState { copy(howToExpanded = !howToExpanded) }

    fun onLanguageTargetChanged(lang: String) {
        viewModelScope.launch {
            repository.updateLanguageTarget(lang)
        }
    }

    fun onSignedIn(name: String, email: String) {
        viewModelScope.launch {
            repository.updateDisplayName(name)
            repository.updateUserEmail(email)
            repository.updateSignInStatus(true)
        }
    }

    fun signInAsGuest() {
        viewModelScope.launch {
            repository.updateDisplayName("Khách")
            repository.updateUserEmail("guest@aienglish.com")
            repository.updateSignInStatus(true)
        }
    }

    fun onSigningIn() = setState { copy(isSigningIn = true, error = null) }
    fun onSignInError(msg: String) = setState { copy(isSigningIn = false, error = msg) }

    fun signOut() {
        viewModelScope.launch {
            repository.clearProfile()
        }
    }

    fun dismissError() = setState { copy(error = null) }
}
