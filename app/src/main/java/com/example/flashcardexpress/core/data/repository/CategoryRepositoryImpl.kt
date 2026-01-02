package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.CategoryDao
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.data.local.mapper.toDomain
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val dao: CategoryDao): CategoryRepository {

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

    override suspend fun updateCategory(newCategory: Category) {
        dao.updateCategory(newCategory.id, newCategory.name)

    }


}