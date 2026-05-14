package com.aura.todonotes.ui.screens.add_edit


import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val title: String = "",
    val content: String = "",
    val colorHex: String = "#FFFFFF",
    val tasks: List<Task> = emptyList(),
    val isPinned: Boolean = false,
    val isLocked: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false,
    val noteId: Long? = null,
    val newTaskContent: String = "",
    val saveComplete: Boolean = false
)

@HiltViewModel
class AddEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {


    private val noteId: Long? = savedStateHandle.get<Long>("noteId")?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(AddEditUiState(
        isEditMode = noteId != null,
        noteId = noteId
    ))
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    init {
        noteId?.let { loadNote(it) }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val note = noteRepository.getNoteById(id).first()
            note?.let {
                val tasks = taskRepository.getTasksByNoteIdSync(id)
                _uiState.value = _uiState.value.copy(
                    title = it.title,
                    content = it.content,
                    colorHex = it.colorHex,
                    isPinned = it.isPinned,
                    isLocked = it.isLocked,
                    tasks = tasks,
                    isLoading = false
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun updateColor(colorHex: String) {
        _uiState.value = _uiState.value.copy(colorHex = colorHex)
    }

    fun togglePin() {
        _uiState.value = _uiState.value.copy(isPinned = !_uiState.value.isPinned)
    }

    fun toggleLock() {
        _uiState.value = _uiState.value.copy(isLocked = !_uiState.value.isLocked)
    }

    fun updateNewTaskContent(content: String) {
        _uiState.value = _uiState.value.copy(newTaskContent = content)
    }

    fun addTask() {
        val content = _uiState.value.newTaskContent.trim()
        if (content.isEmpty()) return

        val newTask = Task(
            noteId = noteId ?: 0,
            content = content,
            position = _uiState.value.tasks.size
        )
        _uiState.value = _uiState.value.copy(
            tasks = _uiState.value.tasks + newTask,
            newTaskContent = ""
        )
    }

    fun toggleTask(index: Int) {
        val tasks = _uiState.value.tasks.toMutableList()
        tasks[index] = tasks[index].copy(isCompleted = !tasks[index].isCompleted)
        _uiState.value = _uiState.value.copy(tasks = tasks)
    }

    fun removeTask(index: Int) {
        val tasks = _uiState.value.tasks.toMutableList()
        tasks.removeAt(index)
        _uiState.value = _uiState.value.copy(tasks = tasks)
    }

    fun saveNote() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)


            val note = Note(
                id = noteId ?: 0,
                title = _uiState.value.title,
                content = _uiState.value.content,
                colorHex = _uiState.value.colorHex,
                isPinned = _uiState.value.isPinned,
                isLocked = _uiState.value.isLocked,
                updatedAt = System.currentTimeMillis()
            )

            val savedNoteId = if (noteId != null) {
                noteRepository.updateNote(note)
                noteId
            } else {
                noteRepository.insertNote(note)
            }

            // Save tasks
            val tasksToSave = _uiState.value.tasks.mapIndexed { index, task ->
                task.copy(noteId = savedNoteId, position = index)
            }
            taskRepository.deleteTasksByNoteId(savedNoteId)
            taskRepository.insertTasks(tasksToSave)

            _uiState.value = _uiState.value.copy(isSaving = false, saveComplete = true)
        }
    }
}
