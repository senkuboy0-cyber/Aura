package com.aura.todonotes.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.model.AppSettings
import com.aura.todonotes.domain.model.ThemeOption
import com.aura.todonotes.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    isLoading = false
                )
            }
        }
    }


    fun setThemeMode(mode: ThemeOption) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setNoteLockEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNoteLockEnabled(enabled)
        }
    }

    fun setPinCode(pin: String?) {
        viewModelScope.launch {
            settingsRepository.setPinCode(pin)
        }
    }
}
