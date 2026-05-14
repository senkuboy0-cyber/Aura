package com.aura.todonotes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aura.todonotes.data.local.dao.NoteDao
import com.aura.todonotes.data.local.dao.NoteTagDao
import com.aura.todonotes.data.local.dao.TagDao
import com.aura.todonotes.data.local.dao.TaskDao
import com.aura.todonotes.data.local.entity.NoteEntity
import com.aura.todonotes.data.local.entity.NoteTagCrossRef
import com.aura.todonotes.data.local.entity.TagEntity
import com.aura.todonotes.data.local.entity.TaskEntity

@Database(
    entities = [
        NoteEntity::class,
        TaskEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun tagDao(): TagDao
    abstract fun noteTagDao(): NoteTagDao
}
