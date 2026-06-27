package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.viewmodel.BaseViewModel

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
    val error: String? = null
)

class ProfileViewModel(
    private val prefs: AppPreferences
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        // Load persisted data on start
        setState {
            copy(
                isSignedIn = prefs.isSignedIn(),
                displayName = prefs.getUserName(),
                email = prefs.getUserEmail(),
                apiKeyInput = prefs.getApiKey()
            )
        }
    }

    fun showProfile() = setState { copy(showProfile = true) }
    fun hideProfile() = setState { copy(showProfile = false, error = null) }

    fun onApiKeyChanged(key: String) = setState { copy(apiKeyInput = key, apiKeySaved = false) }
    fun toggleApiKeyVisibility() = setState { copy(apiKeyVisible = !apiKeyVisible) }

    fun saveApiKey() {
        val key = currentState.apiKeyInput.trim()
        if (key.isEmpty()) { setState { copy(error = "API Key không được để trống") }; return }
        prefs.saveApiKey(key)
        setState { copy(apiKeySaved = true, error = null) }
    }

    fun toggleHowTo() = setState { copy(howToExpanded = !howToExpanded) }

    // Called after successful Google Sign-In
    fun onSignedIn(name: String, email: String) {
        prefs.saveUserName(name)
        prefs.saveUserEmail(email)
        prefs.setSignedIn(true)
        setState { copy(isSignedIn = true, displayName = name, email = email, isSigningIn = false, error = null) }
    }

    fun onSigningIn() = setState { copy(isSigningIn = true, error = null) }
    fun onSignInError(msg: String) = setState { copy(isSigningIn = false, error = msg) }

    fun signOut() {
        prefs.setSignedIn(false)
        prefs.saveUserName(null)
        prefs.saveUserEmail(null)
        setState { copy(isSignedIn = false, displayName = null, email = null, error = null) }
    }

    fun dismissError() = setState { copy(error = null) }
}
