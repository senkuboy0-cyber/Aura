package com.aura.todonotes.domain.model

data class Tag(
    val id: Long = 0,
    val name: String,
    val colorHex: String = "#6200EE",
    val createdAt: Long = System.currentTimeMillis()
)
