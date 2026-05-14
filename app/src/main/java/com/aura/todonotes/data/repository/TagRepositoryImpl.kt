package com.aura.todonotes.data.repository

import com.aura.todonotes.data.local.dao.TagDao
import com.aura.todonotes.data.mapper.toDomain
import com.aura.todonotes.data.mapper.toEntity
import com.aura.todonotes.domain.model.Tag
import com.aura.todonotes.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
) : TagRepository {


    override fun getAllTags(): Flow<List<Tag>> =
        tagDao.getAllTags().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getTagById(id: Long): Tag? =
        tagDao.getTagById(id)?.toDomain()


    override suspend fun insertTag(tag: Tag): Long =
        tagDao.insertTag(tag.toEntity())


    override suspend fun updateTag(tag: Tag) {
        tagDao.updateTag(tag.toEntity())
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(tag.toEntity())
    }

    override suspend fun deleteTagById(id: Long) {
        tagDao.deleteTagById(id)
    }
}
