package com.example.flashcardexpress.feature.questionManagement.domain.usecase.question

import android.util.Log
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import javax.inject.Inject

/**
 * Use case for deleting a flashcard question from the database.
 *
 * Invoked by:
 * - [CategoryDetailsViewModel]
 */
class RemoveQuestionUseCase @Inject constructor(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(questionId: Int) {
        try {
            questionRepository.removeQuestion(questionId)
        }
        catch (ex:Exception)
        {
            Log.e("RemoveCategoryUseCase failed", "invoke: ", ex)
        }


    }
}