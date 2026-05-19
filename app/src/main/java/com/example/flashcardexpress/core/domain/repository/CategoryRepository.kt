package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    /**
     * Inserts a new category into the database.
     *
     * Invoked by:
     * - [AddCategoryUseCase]
     */
    suspend fun insertCategory(categoryName: String, language: String)
    /**
     * Checks if a category with the specified name already exists.
     *
     * Invoked by:
     * - [AddCategoryUseCase]
     * - [UpdateCategoryUseCase]
     */
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean
    /**
     * Checks if a category with the specified name already exists, excluding a specific ID.
     *
     * Invoked by:
     * - [UpdateCategoryUseCase]
     */
    suspend fun isCategoryWithNameExistsExcludingId(categoryName: String, excludeCategoryId: Int): Boolean
    /**
     * Retrieves all categories from the database as a flow.
     *
     * Invoked by:
     * - [GetAllCategoriesUseCase]
     */
    fun getAllCategories(): Flow<List<Category>>
    /**
     * Removes a category and its associated questions from the database.
     *
     * Invoked by:
     * - [RemoveCategoryUseCase]
     */
    suspend fun removeCategory(categoryId: Int)
    /**
     * Updates an existing category's name.
     *
     * Invoked by:
     * - [UpdateCategoryUseCase]
     */
    suspend fun updateCategory(newCategory: Category)

    /**
     * Retrieves a category by its ID.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    suspend fun getCategoryById(categoryId: Int): Category?
}