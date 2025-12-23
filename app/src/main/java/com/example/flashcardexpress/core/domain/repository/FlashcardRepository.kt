package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity
import com.example.flashcardexpress.core.domain.model.Category

interface FlashcardRepository {
    suspend fun insertFlashcard(flashcard: FlashcardEntity)
    suspend fun insertCategory(categoryName: String)
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean

}