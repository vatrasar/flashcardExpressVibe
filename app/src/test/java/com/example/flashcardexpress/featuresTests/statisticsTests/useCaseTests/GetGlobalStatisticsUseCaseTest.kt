package com.example.flashcardexpress.featuresTests.statisticsTests.useCaseTests

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.statistics.domain.usecase.GetGlobalStatisticsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetGlobalStatisticsUseCaseTest {

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
    fun invoke_withCategoriesAndQuestions_returnsCorrectAggregatedStatistics() = runBlocking {
        val categories = listOf(
            Category(id = 1, name = "Spanish Basics", language = "Spanish", learnedWordsCount = 10),
            Category(id = 2, name = "German Basics", language = "German", learnedWordsCount = 5)
        )
        val questions = listOf(
            Question("hola", "hello", 1, 1, 0, LocalDate.now(), LocalDateTime.now()),
            Question("adios", "bye", 2, 1, 1, LocalDate.now().plusDays(1), LocalDateTime.now()),
            Question("gracias", "thanks", 3, 1, 2, LocalDate.now().plusDays(3), LocalDateTime.now()),
            Question("gut", "good", 4, 2, 3, LocalDate.now().plusDays(10), LocalDateTime.now()),
            Question("tag", "day", 5, 2, 4, LocalDate.now().plusDays(30), LocalDateTime.now())
        )
        val categoryRepo = FakeCategoryRepository(categories)
        val questionRepo = FakeQuestionRepository(questions)
        val useCase = GetGlobalStatisticsUseCase(categoryRepo, questionRepo)

        val result = useCase().first()

        assertEquals(20, result.totalWordsCount)
        assertEquals(15, result.learnedWordsCount)
        assertEquals(5, result.activeWordsCount)
        assertEquals(75, result.masteryPercentage)
        assertEquals(1, result.wordsReadyForTodayCount)
        assertEquals(1, result.stageStatistics.stage0TodayOrNewCount)
        assertEquals(1, result.stageStatistics.stage1DayCount)
        assertEquals(1, result.stageStatistics.stage3DaysCount)
        assertEquals(1, result.stageStatistics.stage10DaysCount)
        assertEquals(1, result.stageStatistics.stage30DaysCount)
        assertEquals(15, result.stageStatistics.masteredCount)
        assertEquals(2, result.categoriesStatistics.size)
    }
}
