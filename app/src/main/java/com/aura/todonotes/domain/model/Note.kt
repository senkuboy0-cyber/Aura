package com.aura.todonotes.domain.model

data class Note(
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
    val reminderAt: Long? = null,
    val tasks: List<Task> = emptyList(),
    val tags: List<Tag> = emptyList()
)
