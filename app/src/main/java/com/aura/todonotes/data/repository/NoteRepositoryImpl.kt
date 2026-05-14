package com.aura.todonotes.data.repository


import com.aura.todonotes.data.local.dao.NoteDao
import com.aura.todonotes.data.mapper.toDomain
import com.aura.todonotes.data.mapper.toEntity
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTrashedNotes(): Flow<List<Note>> =
        noteDao.getTrashedNotes().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getArchivedNotes(): Flow<List<Note>> =
        noteDao.getArchivedNotes().map { entities ->
            entities.map { it.toDomain() }
        }


    override fun getNoteById(id: Long): Flow<Note?> =
        noteDao.getNoteByIdFlow(id).map { entity ->
            entity?.toDomain()
        }


    override fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getNotesCount(): Flow<Int> = noteDao.getNotesCount()

    override suspend fun insertNote(note: Note): Long =
        noteDao.insertNote(note.toEntity())

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.copy(updatedAt = System.currentTimeMillis()).toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }


    override suspend fun moveToTrash(id: Long) {
        noteDao.updateTrashStatus(id, true)
    }


    override suspend fun restoreFromTrash(id: Long) {
        noteDao.updateTrashStatus(id, false)
    }


    override suspend fun permanentDelete(id: Long) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }

    override suspend fun togglePin(id: Long) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updatePinStatus(id, !it.isPinned)
        }
    }


    override suspend fun toggleArchive(id: Long) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updateArchiveStatus(id, !it.isArchived)
        }
    }
}
