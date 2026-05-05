package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    /**
     * Inserts a new flashcard question into the database.
     *
     * Invoked by:
     * - [AddQuestionUseCase]
     */
    suspend fun insertQuestion(question: Question)
    /**
     * Retrieves all questions belonging to a specific category as a flow.
     *
     * Invoked by:
     * - [GetAllQuestionsOfCategoryUseCase]
     */
    fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>>

    /**
     * Removes a specific question from the database.
     *
     * Invoked by:
     * - [RemoveQuestionUseCase]
     */
    suspend fun removeQuestion(questionId: Int)

    /**
     * Retrieves a single question by its unique identifier.
     *
     * Invoked by:
     * - [GetQuestionByIdUseCase]
     * - [UpdateQuestionWordAndTranslationUseCase]
     */
    suspend fun getQuestionById(questionId: Int): Question
    /**
     * Updates an existing question's information (text or progress).
     *
     * Invoked by:
     * - [UpdateQuestionWordAndTranslationUseCase]
     */
    suspend fun updateQuestion(question: Question)







}