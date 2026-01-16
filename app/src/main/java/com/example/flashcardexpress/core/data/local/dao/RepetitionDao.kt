package com.example.flashcardexpress.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import kotlinx.coroutines.flow.Flow

@Dao
interface RepetitionDao {
    @Query("SELECT category.name AS categoryName, category.id AS categoryId, COUNT(*) AS count " +
            "From flashcard JOIN category ON flashcard.category_id = category.id" +
            " WHERE date_of_next_repetition <= :currentDate GROUP BY category.id")
    fun getNumberOfQuestionsPerCategoryForToday(currentDate:Long): Flow<List<CategoryWithCount>>

    @Query("SELECT * FROM flashcard WHERE category_id = :categoryId AND date_of_next_repetition <= :currentDate ORDER BY RANDOM() LIMIT :numberOfQuestions")
    suspend fun getQuestionsToLearn(categoryId: Int, currentDate: Long, numberOfQuestions: Int): List<QuestionEntity>

    @Query("UPDATE flashcard SET date_of_next_repetition = :dateOfNextRepetition, learning_master_level = :masterLevel WHERE id = :questionID")
    suspend fun updateQuestionAfterRepetition(questionID:Int, dateOfNextRepetition:Long, masterLevel:Int)
}