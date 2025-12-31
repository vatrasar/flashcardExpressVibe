package com.example.flashcardexpress.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM category WHERE name = :categoryName)")
    suspend fun isCategoryWithNameExists(categoryName: String): Boolean

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category WHERE id = :categoryId")
    suspend fun removeCategory(categoryId: Int)





}