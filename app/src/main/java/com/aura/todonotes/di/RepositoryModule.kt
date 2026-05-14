package com.aura.todonotes.di

import com.aura.todonotes.data.repository.NoteRepositoryImpl
import com.aura.todonotes.data.repository.SettingsRepositoryImpl
import com.aura.todonotes.data.repository.TagRepositoryImpl
import com.aura.todonotes.data.repository.TaskRepositoryImpl
import com.aura.todonotes.domain.repository.NoteRepository
import com.aura.todonotes.domain.repository.SettingsRepository
import com.aura.todonotes.domain.repository.TagRepository
import com.aura.todonotes.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
