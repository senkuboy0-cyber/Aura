package com.aura.todonotes.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.model.SortOrder
import com.aura.todonotes.domain.model.ViewMode
import com.aura.todonotes.domain.repository.NoteRepository
import com.aura.todonotes.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true,
    val viewMode: ViewMode = ViewMode.LIST,
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val showPinnedFirst: Boolean = true,
    val notesCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                noteRepository.getAllNotes(),
                noteRepository.getNotesCount(),
                settingsRepository.getSettings()
            ) { notes, count, settings ->
                val sortedNotes = sortNotes(notes, settings.defaultSort, settings.showPinnedFirst)
                HomeUiState(
                    notes = sortedNotes,
                    isLoading = false,
                    viewMode = settings.defaultView,
                    sortOrder = settings.defaultSort,
                    showPinnedFirst = settings.showPinnedFirst,
                    notesCount = count
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                HomeUiState()
            ).collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun sortNotes(notes: List<Note>, sortOrder: SortOrder, showPinnedFirst: Boolean): List<Note> {
        val sorted = when (sortOrder) {
            SortOrder.NEWEST -> notes.sortedByDescending { it.updatedAt }
            SortOrder.OLDEST -> notes.sortedBy { it.updatedAt }
            SortOrder.ALPHABETICAL_AZ -> notes.sortedBy { it.title.lowercase() }
            SortOrder.ALPHABETICAL_ZA -> notes.sortedByDescending { it.title.lowercase() }
        }
        return if (showPinnedFirst) {
            sorted.sortedByDescending { it.isPinned }
        } else {
            sorted
        }
    }

    fun togglePin(noteId: Long) {
        viewModelScope.launch {
            noteRepository.togglePin(noteId)
        }
    }

    fun moveToTrash(noteId: Long) {
        viewModelScope.launch {
            noteRepository.moveToTrash(noteId)
        }
    }

    fun toggleArchive(noteId: Long) {
        viewModelScope.launch {
            noteRepository.toggleArchive(noteId)
        }
    }
}
