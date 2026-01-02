package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(private val questionDao: QuestionDao): QuestionRepository{

    override suspend fun insertQuestion(question: Question) {
        val questionEntity = QuestionEntity(
            word = question.word,
            translation = question.translation,
            categoryId = question.categoryId
        )
        questionDao.insertQuestion(questionEntity)

    }

}