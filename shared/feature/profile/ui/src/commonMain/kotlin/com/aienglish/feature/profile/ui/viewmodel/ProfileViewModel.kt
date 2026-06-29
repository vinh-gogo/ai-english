package com.aienglish.feature.profile.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.profile.application.usecase.*
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
    val error: String? = null,
    val aiResponseLength: String = "normal"
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase,
    private val updateLanguageTargetUseCase: UpdateLanguageTargetUseCase,
    private val updateLanguageNativeUseCase: UpdateLanguageNativeUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
    private val updateUserEmailUseCase: UpdateUserEmailUseCase,
    private val updateSignInStatusUseCase: UpdateSignInStatusUseCase,
    private val clearProfileUseCase: ClearProfileUseCase,
    private val updatePaidStatusUseCase: UpdatePaidStatusUseCase,
    private val updateAiResponseLengthUseCase: UpdateAiResponseLengthUseCase
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        // Collect reactive user profile
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                setState {
                    copy(
                        isSignedIn = profile.isSignedIn,
                        displayName = profile.name,
                        email = profile.email,
                        languageNative = profile.languageNative,
                        languageTarget = profile.languageTarget,
                        isPaid = profile.isPaid,
                        aiResponseLength = profile.aiResponseLength
                    )
                }
            }
        }
        
        // Load API key async
        viewModelScope.launch {
            val key = getApiKeyUseCase()
            setState { copy(apiKeyInput = key) }
        }
    }

    fun updateMonthlyUsage(usage: Int, limit: Int) {
        setState { copy(monthlyUsage = usage, monthlyLimit = limit) }
    }

    fun setPaid(value: Boolean) {
        viewModelScope.launch {
            updatePaidStatusUseCase(value)
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
            saveApiKeyUseCase(key)
            setState { copy(apiKeySaved = true, error = null) }
        }
    }

    fun toggleHowTo() = setState { copy(howToExpanded = !howToExpanded) }

    fun onLanguageNativeChanged(lang: String) {
        viewModelScope.launch {
            updateLanguageNativeUseCase(lang)
        }
    }

    fun onLanguageTargetChanged(lang: String) {
        viewModelScope.launch {
            updateLanguageTargetUseCase(lang)
        }
    }

    fun onAiResponseLengthChanged(length: String) {
        viewModelScope.launch {
            updateAiResponseLengthUseCase(length)
        }
    }

    fun onSignedIn(name: String, email: String) {
        viewModelScope.launch {
            updateDisplayNameUseCase(name)
            updateUserEmailUseCase(email)
            updateSignInStatusUseCase(true)
        }
    }

    fun signInAsGuest() {
        viewModelScope.launch {
            updateDisplayNameUseCase("Khách")
            updateUserEmailUseCase("guest@aienglish.com")
            updateSignInStatusUseCase(true)
        }
    }

    fun onSigningIn() = setState { copy(isSigningIn = true, error = null) }
    fun onSignInError(msg: String) = setState { copy(isSigningIn = false, error = msg) }

    fun signOut() {
        viewModelScope.launch {
            clearProfileUseCase()
        }
    }

    fun dismissError() = setState { copy(error = null) }
}
