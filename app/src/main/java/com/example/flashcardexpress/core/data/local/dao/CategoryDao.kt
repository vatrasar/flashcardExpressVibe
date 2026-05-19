package com.example.flashcardexpress.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM category WHERE name = :categoryName)")
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM category WHERE name = :categoryName AND id != :excludeCategoryId)")
    suspend fun isCategoryWithNameExistsExcludingId(categoryName: String, excludeCategoryId: Int): Boolean

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category WHERE id = :categoryId")
    suspend fun removeCategory(categoryId: Int)

    @Query("UPDATE category SET name = :newCategoryName, language = :language WHERE id = :categoryId")
    suspend fun updateCategory(categoryId: Int, newCategoryName: String, language: String)

    @Query("SELECT * FROM category WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): CategoryEntity?
}