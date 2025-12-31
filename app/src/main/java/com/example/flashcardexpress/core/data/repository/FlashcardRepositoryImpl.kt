package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.FlashcardDao
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity
import com.example.flashcardexpress.core.data.local.mapper.toDomain
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlashcardRepositoryImpl @Inject constructor(private val dao: FlashcardDao): FlashcardRepository {
    override suspend fun insertFlashcard(flashcard: FlashcardEntity) {
        dao.insertFlashcard(flashcard)

    }

    override suspend fun insertCategory(categoryName: String) {
        val category = CategoryEntity(name = categoryName)
        dao.insertCategory(category)

    }

    override suspend fun isCategoryWithNameExists(categoryName: String): Boolean {
        return dao.isCategoryWithNameExists(categoryName)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun removeCategory(categoryId: Int) {
        dao.removeCategory(categoryId)

    }


}