package com.aura.todonotes.di

import android.content.Context
import androidx.room.Room
import com.aura.todonotes.data.local.AppDatabase
import com.aura.todonotes.data.local.dao.NoteDao
import com.aura.todonotes.data.local.dao.NoteTagDao
import com.aura.todonotes.data.local.dao.TagDao
import com.aura.todonotes.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aura_database"
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }


    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase): TagDao {
        return database.tagDao()
    }

    @Provides
    @Singleton
    fun provideNoteTagDao(database: AppDatabase): NoteTagDao {
        return database.noteTagDao()
    }
}
