package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity
import com.example.flashcardexpress.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    suspend fun insertFlashcard(flashcard: FlashcardEntity)
    suspend fun insertCategory(categoryName: String)
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean
   fun getAllCategories(): Flow<List<Category>>
    suspend fun removeCategory(categoryId: Int)


}