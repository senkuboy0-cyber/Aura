package com.aura.todonotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aura.todonotes.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE noteId = :noteId ORDER BY position ASC")
    fun getTasksByNoteId(noteId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE noteId = :noteId ORDER BY position ASC")
    suspend fun getTasksByNoteIdSync(noteId: Long): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)


    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)


    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("DELETE FROM tasks WHERE noteId = :noteId")
    suspend fun deleteTasksByNoteId(noteId: Long)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean)


    @Query("UPDATE tasks SET position = :position WHERE id = :id")
    suspend fun updateTaskPosition(id: Long, position: Int)
}
