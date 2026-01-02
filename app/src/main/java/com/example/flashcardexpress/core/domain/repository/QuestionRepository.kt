package com.example.flashcardexpress.core.domain.repository

import com.example.flashcardexpress.core.domain.model.Question

interface QuestionRepository {
    suspend fun insertQuestion(question: Question)

}