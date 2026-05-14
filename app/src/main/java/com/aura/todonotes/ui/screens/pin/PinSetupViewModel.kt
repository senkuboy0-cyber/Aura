package com.aura.todonotes.ui.screens.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PinSetupUiState(
    val pin: String = "",
    val confirmPin: String = "",
    val isConfirming: Boolean = false,
    val error: String? = null,
    val isComplete: Boolean = false,
    val hasExistingPin: Boolean = false
)

@HiltViewModel
class PinSetupViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PinSetupUiState())
    val uiState: StateFlow<PinSetupUiState> = _uiState.asStateFlow()

    init {
        checkExistingPin()
    }

    private fun checkExistingPin() {
        viewModelScope.launch {
            settingsRepository.getSettings().first().let { settings ->
                _uiState.value = _uiState.value.copy(
                    hasExistingPin = settings.pinCode != null
                )
            }
        }
    }

    fun onNumberClick(number: String) {
        val state = _uiState.value
        if (state.isConfirming) {
            if (state.confirmPin.length < 4) {
                _uiState.value = state.copy(
                    confirmPin = state.confirmPin + number,
                    error = null
                )
                checkConfirmation()
            }
        } else {
            if (state.pin.length < 4) {
                _uiState.value = state.copy(
                    pin = state.pin + number,
                    error = null
                )
            }
        }
    }

    fun onBackspaceClick() {
        val state = _uiState.value
        if (state.isConfirming) {
            if (state.confirmPin.isNotEmpty()) {
                _uiState.value = state.copy(
                    confirmPin = state.confirmPin.dropLast(1),
                    error = null
                )
            }
        } else {
            if (state.pin.isNotEmpty()) {
                _uiState.value = state.copy(
                    pin = state.pin.dropLast(1),
                    error = null
                )
            }
        }
    }

    fun onClearClick() {
        val state = _uiState.value
        if (state.isConfirming) {
            _uiState.value = state.copy(confirmPin = "", error = null)
        } else {
            _uiState.value = state.copy(pin = "", error = null)
        }
    }

    fun proceedToConfirm() {
        if (_uiState.value.pin.length == 4) {
            _uiState.value = _uiState.value.copy(isConfirming = true)
        }
    }

    private fun checkConfirmation() {
        val state = _uiState.value
        if (state.confirmPin.length == 4) {
            if (state.pin == state.confirmPin) {
                savePin()
            } else {
                _uiState.value = state.copy(
                    confirmPin = "",
                    error = "PINs do not match. Try again."
                )
            }
        }
    }

    private fun savePin() {
        viewModelScope.launch {
            settingsRepository.setPinCode(_uiState.value.pin)
            _uiState.value = _uiState.value.copy(isComplete = true)
        }
    }

    fun removePin() {
        viewModelScope.launch {
            settingsRepository.setPinCode(null)
            _uiState.value = _uiState.value.copy(isComplete = true)
        }
    }
}
