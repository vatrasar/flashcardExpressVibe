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

    /**
     * Retrieves all questions from the database as a flow.
     *
     * Invoked by:
     * - [GetGlobalStatisticsUseCase]
     */
    fun getAllQuestions(): Flow<List<Question>>

    /**
     * Retrieves directly linked question IDs for a specific question.
     *
     * Invoked by:
     * - [GetCategoryQuestionsWithLinkStatusUseCase]
     */
    suspend fun getLinkedQuestionIds(questionId: Int): List<Int>

    /**
     * Retrieves all transitively linked question IDs forming the full connected cluster for a question.
     *
     * Invoked by:
     * - [GetQuestionsToLearnUseCase]
     */
    suspend fun getAllLinkedQuestionIds(questionId: Int): List<Int>

    /**
     * Creates a bidirectional link between two questions.
     *
     * Invoked by:
     * - [ToggleQuestionLinkUseCase]
     */
    suspend fun linkQuestions(questionId: Int, targetQuestionId: Int)

    /**
     * Removes the bidirectional link between two questions.
     *
     * Invoked by:
     * - [ToggleQuestionLinkUseCase]
     */
    suspend fun unlinkQuestions(questionId: Int, targetQuestionId: Int)

    /**
     * Retrieves multiple questions by their IDs.
     *
     * Invoked by:
     * - [GetQuestionsToLearnUseCase]
     */
    suspend fun getQuestionsByIds(questionIds: List<Int>): List<Question>
}