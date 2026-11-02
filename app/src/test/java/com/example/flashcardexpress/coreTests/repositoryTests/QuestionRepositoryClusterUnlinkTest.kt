package com.example.flashcardexpress.coreTests.repositoryTests

import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.dao.QuestionLinkDao
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionLinkEntity
import com.example.flashcardexpress.core.data.repository.QuestionRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionRepositoryClusterUnlinkTest {

    private class FakeQuestionLinkDao : QuestionLinkDao {
        val edges = mutableSetOf<Pair<Int, Int>>()

        override suspend fun getLinkedQuestionIds(questionId: Int): List<Int> {
            return edges.filter { it.first == questionId }.map { it.second }
        }

        override suspend fun getAllLinksForCategory(categoryId: Int): List<QuestionLinkEntity> {
            return edges.map { QuestionLinkEntity(it.first, it.second) }
        }

        override suspend fun insertLink(link: QuestionLinkEntity) {
            edges.add(link.questionId to link.linkedQuestionId)
        }

        override suspend fun deleteBidirectionalLink(questionId1: Int, questionId2: Int) {
            edges.remove(questionId1 to questionId2)
            edges.remove(questionId2 to questionId1)
        }
    }

    private class FakeQuestionDao : QuestionDao {
        override suspend fun insertQuestion(question: QuestionEntity) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<QuestionEntity>> = flowOf(emptyList())
        override fun getAllQuestions(): Flow<List<QuestionEntity>> = flowOf(emptyList())
        override suspend fun removeQuestion(questionId: Int) = Unit
        override suspend fun getQuestionById(questionId: Int): QuestionEntity {
            return QuestionEntity(questionId, 1, "w", "t", 0, 0, 0)
        }
        override suspend fun getQuestionsByIds(questionIds: List<Int>): List<QuestionEntity> = emptyList()
        override suspend fun updateQuestion(question: QuestionEntity) = Unit
    }

    @Test
    fun unlinkQuestions_whenChainA_B_C_removesLinkBetweenB_and_C() = runBlocking {
        val linkDao = FakeQuestionLinkDao()
        val questionDao = FakeQuestionDao()
        val repository = QuestionRepositoryImpl(questionDao, linkDao)

        repository.linkQuestions(1, 2)
        repository.linkQuestions(2, 3)

        assertEquals(setOf(2, 3), repository.getAllLinkedQuestionIds(1).toSet())

        repository.unlinkQuestions(questionId = 1, targetQuestionId = 3)

        assertTrue(linkDao.edges.contains(1 to 2))
        assertTrue(linkDao.edges.contains(2 to 1))
        assertFalse(linkDao.edges.contains(2 to 3))
        assertFalse(linkDao.edges.contains(3 to 2))
        assertEquals(listOf(2), repository.getAllLinkedQuestionIds(1))
        assertTrue(repository.getAllLinkedQuestionIds(3).isEmpty())
    }

    @Test
    fun unlinkQuestions_whenTriangleA_B_C_removesBothLinksToC() = runBlocking {
        val linkDao = FakeQuestionLinkDao()
        val questionDao = FakeQuestionDao()
        val repository = QuestionRepositoryImpl(questionDao, linkDao)

        repository.linkQuestions(1, 2)
        repository.linkQuestions(2, 3)
        repository.linkQuestions(1, 3)

        repository.unlinkQuestions(questionId = 1, targetQuestionId = 3)

        assertTrue(linkDao.edges.contains(1 to 2))
        assertTrue(linkDao.edges.contains(2 to 1))
        assertFalse(linkDao.edges.contains(1 to 3))
        assertFalse(linkDao.edges.contains(3 to 1))
        assertFalse(linkDao.edges.contains(2 to 3))
        assertFalse(linkDao.edges.contains(3 to 2))
        assertEquals(listOf(2), repository.getAllLinkedQuestionIds(1))
    }

    @Test
    fun unlinkQuestions_whenChainA_B_C_D_unlinkingCPreserves_AB_and_CD() = runBlocking {
        val linkDao = FakeQuestionLinkDao()
        val questionDao = FakeQuestionDao()
        val repository = QuestionRepositoryImpl(questionDao, linkDao)

        repository.linkQuestions(1, 2)
        repository.linkQuestions(2, 3)
        repository.linkQuestions(3, 4)

        repository.unlinkQuestions(questionId = 1, targetQuestionId = 3)

        assertTrue(linkDao.edges.contains(1 to 2))
        assertFalse(linkDao.edges.contains(2 to 3))
        assertTrue(linkDao.edges.contains(3 to 4))
        assertEquals(listOf(2), repository.getAllLinkedQuestionIds(1))
        assertEquals(listOf(4), repository.getAllLinkedQuestionIds(3))
    }
}
