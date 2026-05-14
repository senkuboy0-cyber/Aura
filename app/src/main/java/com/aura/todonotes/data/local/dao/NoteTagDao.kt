package com.aura.todonotes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aura.todonotes.data.local.entity.NoteTagCrossRef
import com.aura.todonotes.data.local.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteTagDao {
    @Query("SELECT tags.* FROM tags INNER JOIN note_tag_cross_ref ON tags.id = note_tag_cross_ref.tagId WHERE note_tag_cross_ref.noteId = :noteId")
    fun getTagsByNoteId(noteId: Long): Flow<List<TagEntity>>


    @Query("SELECT tags.* FROM tags INNER JOIN note_tag_cross_ref ON tags.id = note_tag_cross_ref.tagId WHERE note_tag_cross_ref.noteId = :noteId")
    suspend fun getTagsByNoteIdSync(noteId: Long): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteTagCrossRef(crossRef: NoteTagCrossRef)

    @Query("DELETE FROM note_tag_cross_ref WHERE noteId = :noteId AND tagId = :tagId")
    suspend fun deleteNoteTagCrossRef(noteId: Long, tagId: Long)


    @Query("DELETE FROM note_tag_cross_ref WHERE noteId = :noteId")
    suspend fun deleteAllNoteTagCrossRefs(noteId: Long)
}
