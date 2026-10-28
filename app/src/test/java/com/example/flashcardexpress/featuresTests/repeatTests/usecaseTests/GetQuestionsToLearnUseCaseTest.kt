package com.example.flashcardexpress.featuresTests.repeatTests.usecaseTests

import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetQuestionsToLearnUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetQuestionsToLearnUseCaseTest {

    private class FakeRepetitionRepository(
        private val baseQuestions: List<Question>
    ) : RepetitionRepository {
        override fun getNumberOfQuestionsPerCategoryForToday(): Flow<List<CategoryWithCount>> = flowOf(emptyList())
        override suspend fun getQuestionsToLearn(categoryId: Int, numberOfQuestions: Int): List<Question> =
            baseQuestions.take(numberOfQuestions)
        override suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition) = Unit
    }

    private class FakeQuestionRepository(
        private val allQuestions: List<Question>,
        private val linksMap: Map<Int, List<Int>>
    ) : QuestionRepository {
        override suspend fun insertQuestion(question: Question) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> = flowOf(allQuestions)
        override suspend fun removeQuestion(questionId: Int) = Unit
        override suspend fun getQuestionById(questionId: Int): Question = allQuestions.first { it.id == questionId }
        override suspend fun updateQuestion(question: Question) = Unit
        override fun getAllQuestions(): Flow<List<Question>> = flowOf(allQuestions)
        override suspend fun getLinkedQuestionIds(questionId: Int): List<Int> = linksMap[questionId] ?: emptyList()
        override suspend fun getAllLinkedQuestionIds(questionId: Int): List<Int> = linksMap[questionId] ?: emptyList()
        override suspend fun linkQuestions(questionId: Int, targetQuestionId: Int) = Unit
        override suspend fun unlinkQuestions(questionId: Int, targetQuestionId: Int) = Unit
        override suspend fun getQuestionsByIds(questionIds: List<Int>): List<Question> =
            allQuestions.filter { questionIds.contains(it.id) }
    }

    @Test
    fun invoke_whenQuestionHasLinkedQuestions_exceedsSessionLimitToIncludeAllLinked() = runBlocking {
        val q1 = Question("word1", "trans1", 1, 10, 1, LocalDate.now(), LocalDateTime.now())
        val q2 = Question("word2", "trans2", 2, 10, 1, LocalDate.now(), LocalDateTime.now())
        val q3Linked = Question("word3", "trans3", 3, 10, 1, LocalDate.now(), LocalDateTime.now())

        val repRepo = FakeRepetitionRepository(baseQuestions = listOf(q1, q2))
        val questionRepo = FakeQuestionRepository(
            allQuestions = listOf(q1, q2, q3Linked),
            linksMap = mapOf(1 to listOf(3), 3 to listOf(1))
        )
        val useCase = GetQuestionsToLearnUseCase(repRepo, questionRepo)

        val result = useCase(categoryId = 10, numberOfQuestions = 2)

        assertEquals(3, result.size)
        assertTrue(result.any { it.id == 1 })
        assertTrue(result.any { it.id == 2 })
        assertTrue(result.any { it.id == 3 })
    }
}
