package com.aura.todonotes.data.repository


import com.aura.todonotes.data.local.dao.TaskDao
import com.aura.todonotes.data.mapper.toDomain
import com.aura.todonotes.data.mapper.toEntity
import com.aura.todonotes.domain.model.Task
import com.aura.todonotes.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasksByNoteId(noteId: Long): Flow<List<Task>> =
        taskDao.getTasksByNoteId(noteId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getTasksByNoteIdSync(noteId: Long): List<Task> =
        taskDao.getTasksByNoteIdSync(noteId).map { it.toDomain() }


    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(task.toEntity())

    override suspend fun insertTasks(tasks: List<Task>) =
        taskDao.insertTasks(tasks.map { it.toEntity() })

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }


    override suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }

    override suspend fun deleteTasksByNoteId(noteId: Long) {
        taskDao.deleteTasksByNoteId(noteId)
    }

    override suspend fun toggleTaskCompletion(id: Long) {
        val task = taskDao.getTaskById(id)
        task?.let {
            taskDao.updateTaskCompletion(id, !it.isCompleted)
        }
    }

    override suspend fun updateTaskPositions(tasks: List<Task>) {
        tasks.forEachIndexed { index, task ->
            taskDao.updateTaskPosition(task.id, index)
        }
    }
}
