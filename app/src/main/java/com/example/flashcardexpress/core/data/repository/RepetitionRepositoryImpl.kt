package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.RepetitionDao
import com.example.flashcardexpress.core.data.local.mapper.toDomain
import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class RepetitionRepositoryImpl @Inject constructor(private val repetitionDao: RepetitionDao) : RepetitionRepository {
    override fun getNumberOfQuestionsPerCategoryForToday(): Flow<List<CategoryWithCount>> {

        val nowTime= LocalDate.now()
        val timestampLong = nowTime.toEpochDay()
        return repetitionDao.getNumberOfQuestionsPerCategoryForToday(timestampLong)


    }

    override suspend fun getQuestionsToLearn(
        categoryId: Int,
        numberOfQuestions: Int
    ): List<Question> {

        val timeNow=LocalDate.now().toEpochDay()
        val listOfQuestionEntities= repetitionDao.getQuestionsToLearn(categoryId,timeNow,numberOfQuestions)
        return listOfQuestionEntities.map { it.toDomain() }



    }

    override suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition) {
        repetitionDao.updateQuestionAfterRepetition(question.id,question.dateOfNextRepetition,question.masterLevel)

    }
}