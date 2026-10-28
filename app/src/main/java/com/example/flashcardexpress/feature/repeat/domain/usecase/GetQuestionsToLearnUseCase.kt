package com.example.flashcardexpress.feature.repeat.domain.usecase

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.mappers.toQuestionToLearn
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import javax.inject.Inject

/**
 * Use case for retrieving a specific set of questions for a learning session.
 *
 * Fetches the required number of flashcards for a category, expands the set with any
 * linked questions (exceeding the limit if necessary), and converts them into learning-ready format.
 *
 * Invoked by:
 * - [RepetitionViewModel]
 */
class GetQuestionsToLearnUseCase @Inject constructor(
    private val repetitionRepository: RepetitionRepository,
    private val questionRepository: QuestionRepository
) {

    suspend operator fun invoke(categoryId: Int, numberOfQuestions: Int): List<QuestionToLearn> {
        val baseQuestions = repetitionRepository.getQuestionsToLearn(categoryId, numberOfQuestions)
        val allQuestions = expandWithLinkedQuestions(baseQuestions)
        return allQuestions.map { it.toQuestionToLearn() }
    }

    private suspend fun expandWithLinkedQuestions(baseQuestions: List<Question>): List<Question> {
        val presentIds = baseQuestions.map { it.id }.toMutableSet()
        val missingLinkedIds = mutableSetOf<Int>()
        for (question in baseQuestions) {
            collectLinkedIdsForQuestion(question.id, presentIds, missingLinkedIds)
        }
        val extraQuestions = questionRepository.getQuestionsByIds(missingLinkedIds.toList())
        return baseQuestions + extraQuestions
    }

    private suspend fun collectLinkedIdsForQuestion(
        questionId: Int,
        presentIds: Set<Int>,
        missingLinkedIds: MutableSet<Int>
    ) {
        val linkedIds = questionRepository.getAllLinkedQuestionIds(questionId)
        for (linkedId in linkedIds) {
            recordMissingId(linkedId, presentIds, missingLinkedIds)
        }
    }

    private fun recordMissingId(
        linkedId: Int,
        presentIds: Set<Int>,
        missingLinkedIds: MutableSet<Int>
    ) {
        if (!presentIds.contains(linkedId)) {
            missingLinkedIds.add(linkedId)
        }
    }
}
