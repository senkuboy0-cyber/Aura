package com.aura.todonotes.ui.screens.locked

import androidx.lifecycle.SavedStateHandle
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

data class LockedUiState(
    val pin: String = "",
    val error: String? = null,
    val isUnlocked: Boolean = false,
    val isVerifying: Boolean = false
)


@HiltViewModel
class LockedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository
) : ViewModel() {


    private val noteId: Long = savedStateHandle.get<Long>("noteId") ?: 0L
    private val _uiState = MutableStateFlow(LockedUiState())
    val uiState: StateFlow<LockedUiState> = _uiState.asStateFlow()

    fun onNumberClick(number: String) {
        val state = _uiState.value
        if (state.pin.length < 4) {
            _uiState.value = state.copy(pin = state.pin + number, error = null)
            if (state.pin.length + 1 == 4) {
                verifyPin()
            }
        }
    }

    fun onBackspaceClick() {
        val state = _uiState.value
        if (state.pin.isNotEmpty()) {
            _uiState.value = state.copy(pin = state.pin.dropLast(1), error = null)
        }
    }

    fun onClearClick() {
        _uiState.value = _uiState.value.copy(pin = "", error = null)
    }

    private fun verifyPin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifying = true)
            val settings = settingsRepository.getSettings().first()
            if (settings.pinCode == _uiState.value.pin) {
                _uiState.value = _uiState.value.copy(isUnlocked = true, isVerifying = false)
            } else {
                _uiState.value = _uiState.value.copy(
                    pin = "",
                    error = "Incorrect PIN. Try again.",
                    isVerifying = false
                )
            }
        }
    }
}
