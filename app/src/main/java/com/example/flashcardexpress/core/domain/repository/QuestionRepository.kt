package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insertQuestion(question: Question)
    fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>>



}