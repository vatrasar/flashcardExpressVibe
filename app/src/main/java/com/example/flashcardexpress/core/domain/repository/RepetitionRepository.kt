package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import kotlinx.coroutines.flow.Flow

interface RepetitionRepository {
    fun getNumberOfQuestionsPerCategoryForToday(): Flow<List<CategoryWithCount>>
    suspend fun getQuestionsToLearn(categoryId: Int, numberOfQuestions: Int): List<Question>

    suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition)



}