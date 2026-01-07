package com.example.flashcardexpress.feature.questionManagement.domain.usecase.question

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllQuestionsOfCategoryUseCase @Inject constructor(private val questionRepository: QuestionRepository) {
    operator fun invoke(categoryId: Int): Flow<List<Question>> {

        return questionRepository.getAllQuestionsOfCategory(categoryId)
    }
}