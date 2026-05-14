package com.aura.todonotes.domain.model

data class Task(
    val id: Long = 0,
    val noteId: Long,
    val content: String,
    val isCompleted: Boolean = false,
    val position: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
