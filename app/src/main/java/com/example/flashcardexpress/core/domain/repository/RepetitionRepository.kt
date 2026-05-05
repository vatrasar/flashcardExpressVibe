package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import kotlinx.coroutines.flow.Flow

interface RepetitionRepository {
    /**
     * Retrieves the number of questions ready for repetition today, grouped by category.
     *
     * Invoked by:
     * - [GetAllRepetitionsForTodayUseCase]
     */
    fun getNumberOfQuestionsPerCategoryForToday(): Flow<List<CategoryWithCount>>
    /**
     * Retrieves a specific number of questions from a category for a learning session.
     *
     * Invoked by:
     * - [GetQuestionsToLearnUseCase]
     */
    suspend fun getQuestionsToLearn(categoryId: Int, numberOfQuestions: Int): List<Question>

    /**
     * Updates a question's repetition data (next date and level) after a learning session.
     *
     * Invoked by:
     * - [RepetitionSessionManager]
     */
    suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition)



}