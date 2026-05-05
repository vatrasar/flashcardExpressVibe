package com.example.flashcardexpress.feature.questionManagement.domain.usecase.question

import android.util.Log
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for retrieving a specific flashcard question by its unique identifier.
 *
 * Invoked by:
 * - [QuestionEditViewModel]
 */
class GetQuestionByIdUseCase @Inject constructor(val repository: QuestionRepository) {
    suspend operator fun invoke(id: Int): Question {
        try {
            return repository.getQuestionById(id)
        } catch (e: Exception) {
            Log.e("GetQuestionByIdUseCase", "Error getting question by ID", e)
            return Question("", "", 0,0,0, LocalDate.now())

        }
    }

}