package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun insertCategory(categoryName: String)
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean
   fun getAllCategories(): Flow<List<Category>>
    suspend fun removeCategory(categoryId: Int)
    suspend fun updateCategory(newCategory: Category)


}