package com.example.flashcardexpress.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("SELECT * FROM flashcard WHERE category_id = :categoryId")
    fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<QuestionEntity>>



}