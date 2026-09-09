package com.example.flashcardexpress.featuresTests.statisticsTests.useCaseTests

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.statistics.domain.usecase.GetCategoryStatisticsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetCategoryStatisticsUseCaseTest {

    private class FakeCategoryRepository(
        private val categories: List<Category>
    ) : CategoryRepository {
        override suspend fun insertCategory(categoryName: String, language: String) = Unit
        override suspend fun isCategoryWithNameExists(categoryName: String) = false
        override suspend fun isCategoryWithNameExistsExcludingId(categoryName: String, excludeCategoryId: Int) = false
        override fun getAllCategories(): Flow<List<Category>> = flowOf(categories)
        override suspend fun removeCategory(categoryId: Int) = Unit
        override suspend fun updateCategory(newCategory: Category) = Unit
        override suspend fun getCategoryById(categoryId: Int) = categories.find { it.id == categoryId }
        override fun getCategoryFlowById(categoryId: Int): Flow<Category?> = flowOf(categories.find { it.id == categoryId })
        override suspend fun incrementLearnedWordsCount(categoryId: Int) = Unit
        override fun getGlobalLearnedWordsCount(): Flow<Int> = flowOf(categories.sumOf { it.learnedWordsCount })
    }

    private class FakeQuestionRepository(
        private val questions: List<Question>
    ) : QuestionRepository {
        override suspend fun insertQuestion(question: Question) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> =
            flowOf(questions.filter { it.categoryId == categoryId })
        override suspend fun removeQuestion(questionId: Int) = Unit
        override suspend fun getQuestionById(questionId: Int) = questions.first { it.id == questionId }
        override suspend fun updateQuestion(question: Question) = Unit
        override fun getAllQuestions(): Flow<List<Question>> = flowOf(questions)
    }

    @Test
    fun invoke_withValidCategoryId_returnsCalculatedCategoryStatistics() = runBlocking {
        val category = Category(id = 1, name = "Spanish Basics", language = "Spanish", learnedWordsCount = 4)
        val questions = listOf(
            Question("hola", "hello", 1, 1, 0, LocalDate.now(), LocalDateTime.now()),
            Question("adios", "bye", 2, 1, 2, LocalDate.now().plusDays(3), LocalDateTime.now())
        )
        val categoryRepo = FakeCategoryRepository(listOf(category))
        val questionRepo = FakeQuestionRepository(questions)
        val useCase = GetCategoryStatisticsUseCase(categoryRepo, questionRepo)

        val result = useCase(1).first()

        assertNotNull(result)
        assertEquals(1, result?.categoryId)
        assertEquals("Spanish Basics", result?.categoryName)
        assertEquals("Spanish", result?.language)
        assertEquals(6, result?.totalWordsCount)
        assertEquals(4, result?.learnedWordsCount)
        assertEquals(2, result?.activeWordsCount)
        assertEquals(66, result?.masteryPercentage)
        assertEquals(1, result?.wordsReadyForTodayCount)
        assertEquals(1, result?.stageStatistics?.stage0TodayOrNewCount)
        assertEquals(1, result?.stageStatistics?.stage3DaysCount)
        assertEquals(4, result?.stageStatistics?.masteredCount)
    }

    @Test
    fun invoke_withNonExistentCategoryId_returnsNull() = runBlocking {
        val categoryRepo = FakeCategoryRepository(emptyList())
        val questionRepo = FakeQuestionRepository(emptyList())
        val useCase = GetCategoryStatisticsUseCase(categoryRepo, questionRepo)

        val result = useCase(999).first()

        assertNull(result)
    }
}
