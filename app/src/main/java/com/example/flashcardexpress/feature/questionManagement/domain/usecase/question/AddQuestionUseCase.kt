package com.example.flashcardexpress.feature.questionManagement.domain.usecase.question

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import javax.inject.Inject

/**
 * Use case for adding a new flashcard question to the database.
 *
 * Invoked by:
 * - [CreationQuestionViewModel]
 */
class AddQuestionUseCase @Inject constructor(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(question: Question):Result<Unit> {
        try {
            questionRepository.insertQuestion(question)
            return Result.success(Unit)
        } catch (ex: Exception) {
            return Result.failure(ex)

        }

    }

}