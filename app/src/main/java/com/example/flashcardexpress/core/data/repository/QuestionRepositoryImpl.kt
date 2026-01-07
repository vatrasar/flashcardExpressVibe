package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.data.local.mapper.toDomain
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> {

        return questionDao.getAllQuestionsOfCategory(categoryId).map{entities->
            entities.map{
                it.toDomain()
            }

        }


    }

}