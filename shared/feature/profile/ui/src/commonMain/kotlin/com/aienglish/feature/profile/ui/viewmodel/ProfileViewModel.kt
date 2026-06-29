package com.aienglish.feature.profile.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.core.designsystem.theme.AppTheme
import com.aienglish.feature.profile.application.usecase.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ProfileState(
    val showProfile: Boolean = false,
    val isSignedIn: Boolean = false,
    val displayName: String? = null,
    val email: String? = null,
    val apiKeyInput: String = "",
    val apiKeyVisible: Boolean = false,
    val apiKeySaved: Boolean = false,
    val novitaApiKeyInput: String = "",
    val novitaApiKeyVisible: Boolean = false,
    val novitaApiKeySaved: Boolean = false,
    val apiProvider: String = "gemini",
    val howToExpanded: Boolean = false,
    val isSigningIn: Boolean = false,
    val languageNative: String = "vi",
    val languageTarget: String = "en",
    val monthlyUsage: Int = 0,
    val monthlyLimit: Int = 5,
    val isPaid: Boolean = false,
    val error: String? = null,
    val aiResponseLength: String = "normal",
    val appTheme: AppTheme = AppTheme.SUNSET_WARMTH
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
    private val updateAiResponseLengthUseCase: UpdateAiResponseLengthUseCase,
    private val getNovitaApiKeyUseCase: GetNovitaApiKeyUseCase,
    private val saveNovitaApiKeyUseCase: SaveNovitaApiKeyUseCase,
    private val updateApiProviderUseCase: UpdateApiProviderUseCase,
    private val appPreferences: AppPreferences
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
                        aiResponseLength = profile.aiResponseLength,
                        apiProvider = profile.apiProvider
                    )
                }
            }
        }
        
        // Load API key async
        viewModelScope.launch {
            val key = getApiKeyUseCase()
            setState { copy(apiKeyInput = key) }
        }

        // Load Novita API key async
        viewModelScope.launch {
            val key = getNovitaApiKeyUseCase()
            setState { copy(novitaApiKeyInput = key) }
        }

        // Load saved theme
        appPreferences.appThemeFlow.onEach { themeName ->
            val theme = AppTheme.entries.find { it.name == themeName } ?: AppTheme.SUNSET_WARMTH
            setState { copy(appTheme = theme) }
        }.launchIn(viewModelScope)
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

    fun onNovitaApiKeyChanged(key: String) = setState { copy(novitaApiKeyInput = key, novitaApiKeySaved = false) }
    fun toggleNovitaApiKeyVisibility() = setState { copy(novitaApiKeyVisible = !novitaApiKeyVisible) }

    fun saveNovitaApiKey() {
        val key = currentState.novitaApiKeyInput.trim()
        if (key.isEmpty()) { setState { copy(error = "Novita API Key không được để trống") }; return }
        viewModelScope.launch {
            saveNovitaApiKeyUseCase(key)
            setState { copy(novitaApiKeySaved = true, error = null) }
        }
    }

    fun onApiProviderChanged(provider: String) {
        viewModelScope.launch {
            updateApiProviderUseCase(provider)
        }
    }

    fun onThemeChanged(theme: AppTheme) {
        appPreferences.saveAppTheme(theme.name)
        setState { copy(appTheme = theme) }
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
