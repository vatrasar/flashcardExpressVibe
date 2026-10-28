package com.example.flashcardexpress.core.data.repository

import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.dao.QuestionLinkDao
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionLinkEntity
import com.example.flashcardexpress.core.data.local.mapper.toDomain
import com.example.flashcardexpress.core.data.local.mapper.toEntity
import com.example.flashcardexpress.core.data.local.mapper.toEntityWithNoId
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val questionLinkDao: QuestionLinkDao
) : QuestionRepository {

    override suspend fun insertQuestion(question: Question) {
        val questionEntity = question.toEntityWithNoId()
        questionDao.insertQuestion(questionEntity)

    }

    override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> {

        return questionDao.getAllQuestionsOfCategory(categoryId).map{entities->
            entities.map{
                it.toDomain()
            }

        }


    }

    override suspend fun removeQuestion(questionId: Int) {
        questionDao.removeQuestion(questionId)

    }

    override suspend fun getQuestionById(questionId: Int): Question {
        return questionDao.getQuestionById(questionId).toDomain()
    }

    override suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question.toEntity())
    }

    override fun getAllQuestions(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLinkedQuestionIds(questionId: Int): List<Int> {
        return questionLinkDao.getLinkedQuestionIds(questionId)
    }

    override suspend fun getAllLinkedQuestionIds(questionId: Int): List<Int> {
        val visited = mutableSetOf(questionId)
        val queue = ArrayDeque<Int>()
        queue.add(questionId)
        while (queue.isNotEmpty()) {
            exploreNextClusterNode(queue.removeFirst(), visited, queue)
        }
        visited.remove(questionId)
        return visited.toList()
    }

    override suspend fun linkQuestions(questionId: Int, targetQuestionId: Int) {
        questionLinkDao.insertLink(QuestionLinkEntity(questionId, targetQuestionId))
        questionLinkDao.insertLink(QuestionLinkEntity(targetQuestionId, questionId))
    }

    override suspend fun unlinkQuestions(questionId: Int, targetQuestionId: Int) {
        questionLinkDao.deleteBidirectionalLink(questionId, targetQuestionId)
    }

    override suspend fun getQuestionsByIds(questionIds: List<Int>): List<Question> {
        if (questionIds.isEmpty()) return emptyList()
        return questionDao.getQuestionsByIds(questionIds).map { it.toDomain() }
    }

    private suspend fun exploreNextClusterNode(
        nodeId: Int,
        visited: MutableSet<Int>,
        queue: ArrayDeque<Int>
    ) {
        val neighbors = questionLinkDao.getLinkedQuestionIds(nodeId)
        for (neighbor in neighbors) {
            enqueueUnvisitedNeighbor(neighbor, visited, queue)
        }
    }

    private fun enqueueUnvisitedNeighbor(
        neighborId: Int,
        visited: MutableSet<Int>,
        queue: ArrayDeque<Int>
    ) {
        if (visited.add(neighborId)) {
            queue.add(neighborId)
        }
    }
}