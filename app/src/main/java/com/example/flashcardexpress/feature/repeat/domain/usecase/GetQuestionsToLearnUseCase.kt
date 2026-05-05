package com.example.flashcardexpress.feature.repeat.domain.usecase

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.mappers.toQuestionToLearn
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for retrieving a specific set of questions for a learning session.
 *
 * Fetches the required number of flashcards for a category and converts them into the learning-ready format.
 *
 * Invoked by:
 * - [RepetitionViewModel]
 */
class GetQuestionsToLearnUseCase @Inject constructor(private val repetitionRepository: RepetitionRepository) {
    suspend operator fun invoke(categoryId: Int,numberOfQuestions:Int): List<QuestionToLearn> {
        var listOfQuestions=listOf<Question>()

        listOfQuestions =
                repetitionRepository.getQuestionsToLearn(categoryId, numberOfQuestions)


        return listOfQuestions.map {
            it.toQuestionToLearn()

        }
    }
}