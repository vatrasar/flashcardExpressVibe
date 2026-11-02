package com.example.flashcardexpress.feature.repeat.domain.usecase

import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionWithLinkStatus
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Retrieves all questions for a category (excluding the source question) paired with their link status.
 *
 * Linked questions appear first (alphabetically), followed by unlinked questions (alphabetically).
 *
 * Invoked by:
 * - [LinkQuestionsViewModel]
 */
class GetCategoryQuestionsWithLinkStatusUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {

    suspend operator fun invoke(categoryId: Int, sourceQuestionId: Int): List<QuestionWithLinkStatus> {
        val linkedIds = questionRepository.getAllLinkedQuestionIds(sourceQuestionId).toSet()
        val categoryQuestions = questionRepository.getAllQuestionsOfCategory(categoryId).first()
        return categoryQuestions
            .filter { it.id != sourceQuestionId }
            .map { QuestionWithLinkStatus(it, linkedIds.contains(it.id)) }
            .sortedWith(
                compareByDescending<QuestionWithLinkStatus> { it.isLinked }
                    .thenBy(String.CASE_INSENSITIVE_ORDER) { it.question.word }
            )
    }
}
