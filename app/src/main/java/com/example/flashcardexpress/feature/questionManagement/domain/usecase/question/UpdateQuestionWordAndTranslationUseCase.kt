package com.example.flashcardexpress.feature.questionManagement.domain.usecase.question

import android.util.Log
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import javax.inject.Inject

class UpdateQuestionWordAndTranslationUseCase @Inject constructor(private val questionRepository: QuestionRepository) {
    suspend operator fun invoke(question: Question)
    {

        try {
            val previousQuestion=questionRepository.getQuestionById(question.id)
            val newVersionOfQuestion=question.copy(dateOfNextRepetition = previousQuestion.dateOfNextRepetition, learningMasterLevel = previousQuestion.learningMasterLevel)
            questionRepository.updateQuestion(newVersionOfQuestion)
        } catch (e: Exception) {
            Log.e("UpdateQuestionUseCase", "Error updating question", e)

        }
    }
}