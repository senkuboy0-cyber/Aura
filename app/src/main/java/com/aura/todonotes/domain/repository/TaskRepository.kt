package com.aura.todonotes.domain.repository

import com.aura.todonotes.domain.model.Task
import kotlinx.coroutines.flow.Flow


interface TaskRepository {
    fun getTasksByNoteId(noteId: Long): Flow<List<Task>>
    suspend fun getTasksByNoteIdSync(noteId: Long): List<Task>
    suspend fun insertTask(task: Task): Long
    suspend fun insertTasks(tasks: List<Task>)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(id: Long)
    suspend fun toggleTaskCompletion(id: Long)
    suspend fun updateTaskPositions(tasks: List<Task>)
}
