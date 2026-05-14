package com.aura.todonotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val colorHex: String = "#FFFFFF",
    val isPinned: Boolean = false,
    val isLocked: Boolean = false,
    val isArchived: Boolean = false,
    val isInTrash: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val reminderAt: Long? = null
)
