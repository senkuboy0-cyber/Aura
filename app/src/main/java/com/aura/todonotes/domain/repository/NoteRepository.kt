package com.aura.todonotes.domain.repository

import com.aura.todonotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getTrashedNotes(): Flow<List<Note>>
    fun getArchivedNotes(): Flow<List<Note>>
    fun getNoteById(id: Long): Flow<Note?>
    fun searchNotes(query: String): Flow<List<Note>>
    fun getNotesCount(): Flow<Int>

    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNoteById(id: Long)
    suspend fun moveToTrash(id: Long)
    suspend fun restoreFromTrash(id: Long)
    suspend fun permanentDelete(id: Long)
    suspend fun emptyTrash()
    suspend fun togglePin(id: Long)
    suspend fun toggleArchive(id: Long)
}
