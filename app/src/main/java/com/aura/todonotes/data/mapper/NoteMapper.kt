package com.aura.todonotes.data.mapper

import com.aura.todonotes.data.local.entity.NoteEntity
import com.aura.todonotes.data.local.entity.TaskEntity
import com.aura.todonotes.data.local.entity.TagEntity
import com.aura.todonotes.domain.model.Note
import com.aura.todonotes.domain.model.Task
import com.aura.todonotes.domain.model.Tag

fun NoteEntity.toDomain(tasks: List<Task> = emptyList(), tags: List<Tag> = emptyList()): Note = Note(
    id = id,
    title = title,
    content = content,
    colorHex = colorHex,
    isPinned = isPinned,
    isLocked = isLocked,
    isArchived = isArchived,
    isInTrash = isInTrash,
    createdAt = createdAt,
    updatedAt = updatedAt,
    reminderAt = reminderAt,
    tasks = tasks,
    tags = tags
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    colorHex = colorHex,
    isPinned = isPinned,
    isLocked = isLocked,
    isArchived = isArchived,
    isInTrash = isInTrash,
    createdAt = createdAt,
    updatedAt = updatedAt,
    reminderAt = reminderAt
)


fun TaskEntity.toDomain(): Task = Task(
    id = id,
    noteId = noteId,
    content = content,
    isCompleted = isCompleted,
    position = position,
    createdAt = createdAt
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    noteId = noteId,
    content = content,
    isCompleted = isCompleted,
    position = position,
    createdAt = createdAt
)

fun TagEntity.toDomain(): Tag = Tag(
    id = id,
    name = name,
    colorHex = colorHex,
    createdAt = createdAt
)

fun Tag.toEntity(): TagEntity = TagEntity(
    id = id,
    name = name,
    colorHex = colorHex,
    createdAt = createdAt
)
