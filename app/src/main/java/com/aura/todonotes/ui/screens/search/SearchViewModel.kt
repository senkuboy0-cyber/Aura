package com.aura.todonotes.ui.screens.search


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SearchUiState(
    val query: String = "",
    val results: List<Note> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        searchJob?.cancel()
        if (query.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                search(query)
            }
        } else {
            _uiState.value = _uiState.value.copy(
                results = emptyList(),
                hasSearched = false
            )
        }
    }

    private suspend fun search(query: String) {
        _uiState.value = _uiState.value.copy(isSearching = true)
        noteRepository.searchNotes(query).collect { notes ->
            _uiState.value = _uiState.value.copy(
                results = notes,
                isSearching = false,
                hasSearched = true
            )
        }
    }

    fun clearQuery() {
        _uiState.value = SearchUiState()
    }
}
