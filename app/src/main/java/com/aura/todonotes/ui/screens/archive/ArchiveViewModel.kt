package com.aura.todonotes.ui.screens.archive

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

data class ArchiveUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    init {
        loadArchive()
    }

    private fun loadArchive() {
        viewModelScope.launch {
            noteRepository.getArchivedNotes().collect { notes ->
                _uiState.value = _uiState.value.copy(
                    notes = notes,
                    isLoading = false
                )
            }
        }
    }

    fun unarchiveNote(noteId: Long) {
        viewModelScope.launch {
            noteRepository.toggleArchive(noteId)
        }
    }
}
