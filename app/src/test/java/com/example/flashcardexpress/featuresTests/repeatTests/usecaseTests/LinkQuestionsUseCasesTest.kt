package com.example.flashcardexpress.featuresTests.repeatTests.usecaseTests

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetCategoryQuestionsWithLinkStatusUseCase
import com.example.flashcardexpress.feature.repeat.domain.usecase.ToggleQuestionLinkUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class LinkQuestionsUseCasesTest {

    private class FakeQuestionRepository(
        private val questions: List<Question> = emptyList(),
        private val linkedIds: MutableSet<Int> = mutableSetOf()
    ) : QuestionRepository {
        var lastLinkedPair: Pair<Int, Int>? = null
        var lastUnlinkedPair: Pair<Int, Int>? = null

        override suspend fun insertQuestion(question: Question) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> = flowOf(questions)
        override suspend fun removeQuestion(questionId: Int) = Unit
        override suspend fun getQuestionById(questionId: Int): Question = questions.first { it.id == questionId }
        override suspend fun updateQuestion(question: Question) = Unit
        override fun getAllQuestions(): Flow<List<Question>> = flowOf(questions)
        override suspend fun getLinkedQuestionIds(questionId: Int): List<Int> = linkedIds.toList()
        override suspend fun getAllLinkedQuestionIds(questionId: Int): List<Int> = linkedIds.toList()
        override suspend fun linkQuestions(questionId: Int, targetQuestionId: Int) {
            lastLinkedPair = questionId to targetQuestionId
            linkedIds.add(targetQuestionId)
        }
        override suspend fun unlinkQuestions(questionId: Int, targetQuestionId: Int) {
            lastUnlinkedPair = questionId to targetQuestionId
            linkedIds.remove(targetQuestionId)
        }
        override suspend fun getQuestionsByIds(questionIds: List<Int>): List<Question> =
            questions.filter { questionIds.contains(it.id) }
    }

    @Test
    fun getCategoryQuestionsWithLinkStatus_excludesSourceQuestionAndMarksLinkedStatus() = runBlocking {
        val qSource = Question("source", "źródło", 1, 10, 1, LocalDate.now(), LocalDateTime.now())
        val qLinked = Question("target1", "cel1", 2, 10, 1, LocalDate.now(), LocalDateTime.now())
        val qUnlinked = Question("target2", "cel2", 3, 10, 1, LocalDate.now(), LocalDateTime.now())

        val repo = FakeQuestionRepository(
            questions = listOf(qSource, qLinked, qUnlinked),
            linkedIds = mutableSetOf(2)
        )
        val useCase = GetCategoryQuestionsWithLinkStatusUseCase(repo)

        val result = useCase(categoryId = 10, sourceQuestionId = 1)

        assertEquals(2, result.size)
        assertFalse(result.any { it.question.id == 1 })

        val itemLinked = result.first { it.question.id == 2 }
        val itemUnlinked = result.first { it.question.id == 3 }
        assertTrue(itemLinked.isLinked)
        assertFalse(itemUnlinked.isLinked)
    }

    @Test
    fun toggleQuestionLink_whenShouldLinkTrue_linksQuestions() = runBlocking {
        val repo = FakeQuestionRepository()
        val useCase = ToggleQuestionLinkUseCase(repo)

        useCase(sourceQuestionId = 1, targetQuestionId = 2, shouldLink = true)

        assertEquals(1 to 2, repo.lastLinkedPair)
    }

    @Test
    fun toggleQuestionLink_whenShouldLinkFalse_unlinksQuestions() = runBlocking {
        val repo = FakeQuestionRepository()
        val useCase = ToggleQuestionLinkUseCase(repo)

        useCase(sourceQuestionId = 1, targetQuestionId = 2, shouldLink = false)

        assertEquals(1 to 2, repo.lastUnlinkedPair)
    }
}
