package com.aura.todonotes.ui.screens.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrashUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true,
    val showEmptyTrashDialog: Boolean = false
)

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrashUiState())
    val uiState: StateFlow<TrashUiState> = _uiState.asStateFlow()

    init {
        loadTrash()
    }

    private fun loadTrash() {
        viewModelScope.launch {
            noteRepository.getTrashedNotes().collect { notes ->
                _uiState.value = _uiState.value.copy(
                    notes = notes,
                    isLoading = false
                )
            }
        }
    }

    fun restoreNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.restoreFromTrash(noteId)
        }
    }

    fun permanentDelete(noteId: Long) {
        viewModelScope.launch {
            noteRepository.permanentDelete(noteId)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            noteRepository.emptyTrash()
            _uiState.value = _uiState.value.copy(showEmptyTrashDialog = false)
        }
    }

    fun showEmptyTrashDialog() {
        _uiState.value = _uiState.value.copy(showEmptyTrashDialog = true)
    }

    fun hideEmptyTrashDialog() {
        _uiState.value = _uiState.value.copy(showEmptyTrashDialog = false)
    }
}
