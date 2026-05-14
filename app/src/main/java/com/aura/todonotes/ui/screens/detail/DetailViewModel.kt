package com.aura.todonotes.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.model.Task
import com.aura.todonotes.domain.repository.NoteRepository
import com.aura.todonotes.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val note: Note? = null,
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentNoteId: Long = 0

    fun loadNote(noteId: Long) {
        currentNoteId = noteId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            noteRepository.getNoteById(noteId).collect { note ->
                if (note != null) {
                    val tasks = taskRepository.getTasksByNoteIdSync(noteId)
                    _uiState.value = _uiState.value.copy(
                        note = note,
                        tasks = tasks,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun toggleTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.toggleTaskCompletion(taskId)
            loadNote(currentNoteId)
        }
    }

    fun moveToTrash() {
        viewModelScope.launch {
            noteRepository.moveToTrash(currentNoteId)
            _uiState.value = _uiState.value.copy(isDeleted = true)
        }
    }

    fun togglePin() {
        viewModelScope.launch {
            noteRepository.togglePin(currentNoteId)
            loadNote(currentNoteId)
        }
    }

    fun toggleArchive() {
        viewModelScope.launch {
            noteRepository.toggleArchive(currentNoteId)
            loadNote(currentNoteId)
        }
    }
}
