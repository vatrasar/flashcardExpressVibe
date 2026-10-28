package com.example.flashcardexpress.feature.repeat.domain.usecase

import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import javax.inject.Inject

/**
 * Toggles the link between a source question and a target question.
 *
 * Invoked by:
 * - [LinkQuestionsViewModel]
 */
class ToggleQuestionLinkUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {

    suspend operator fun invoke(sourceQuestionId: Int, targetQuestionId: Int, shouldLink: Boolean) {
        if (shouldLink) {
            questionRepository.linkQuestions(sourceQuestionId, targetQuestionId)
        } else {
            questionRepository.unlinkQuestions(sourceQuestionId, targetQuestionId)
        }
    }
}
