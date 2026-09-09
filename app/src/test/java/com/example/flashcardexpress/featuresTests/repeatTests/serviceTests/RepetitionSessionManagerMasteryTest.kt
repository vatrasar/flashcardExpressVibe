package com.example.flashcardexpress.featuresTests.repeatTests.serviceTests

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.RepetitionSessionManager
import com.example.flashcardexpress.feature.repeat.domain.model.LearningSessionBackupState
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import com.example.flashcardexpress.feature.repeat.domain.model.enums.LearningStage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class RepetitionSessionManagerMasteryTest {

    private class FakeCategoryRepository : CategoryRepository {
        var incrementedCategoryId: Int? = null

        override suspend fun insertCategory(categoryName: String, language: String) = Unit
        override suspend fun isCategoryWithNameExists(categoryName: String) = false
        override suspend fun isCategoryWithNameExistsExcludingId(categoryName: String, excludeCategoryId: Int) = false
        override fun getAllCategories(): Flow<List<Category>> = flowOf(emptyList())
        override suspend fun removeCategory(categoryId: Int) = Unit
        override suspend fun updateCategory(newCategory: Category) = Unit
        override suspend fun getCategoryById(categoryId: Int): Category? = null
        override fun getCategoryFlowById(categoryId: Int): Flow<Category?> = flowOf(null)
        override suspend fun incrementLearnedWordsCount(categoryId: Int) {
            incrementedCategoryId = categoryId
        }
        override fun getGlobalLearnedWordsCount(): Flow<Int> = flowOf(0)
    }

    private class FakeQuestionRepository(
        private val question: Question
    ) : QuestionRepository {
        var removedQuestionId: Int? = null

        override suspend fun insertQuestion(question: Question) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> = flowOf(emptyList())
        override suspend fun removeQuestion(questionId: Int) {
            removedQuestionId = questionId
        }
        override suspend fun getQuestionById(questionId: Int) = question
        override suspend fun updateQuestion(question: Question) = Unit
        override fun getAllQuestions(): Flow<List<Question>> = flowOf(emptyList())
    }

    private class FakeRepetitionRepository : RepetitionRepository {
        override fun getNumberOfQuestionsPerCategoryForToday() = flowOf(emptyList<com.example.flashcardexpress.core.domain.model.CategoryWithCount>())
        override suspend fun getQuestionsToLearn(categoryId: Int, numberOfQuestions: Int) = emptyList<Question>()
        override suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition) = Unit
    }

    @Test
    fun processUserAnswer_whenMasteryLevelGreaterThanThreeAndCorrect_incrementsCategoryLearnedCountAndRemovesQuestion() = runBlocking {
        val masteredQuestion = Question(
            word = "perro",
            translation = "dog",
            id = 42,
            categoryId = 7,
            learningMasterLevel = 4,
            dateOfNextRepetition = LocalDate.now(),
            createdAt = LocalDateTime.now()
        )
        val fakeCatRepo = FakeCategoryRepository()
        val fakeQuestionRepo = FakeQuestionRepository(masteredQuestion)
        val fakeRepRepo = FakeRepetitionRepository()

        val manager = RepetitionSessionManager(fakeRepRepo, fakeQuestionRepo, fakeCatRepo)
        val questionToLearn = QuestionToLearn(
            id = 42,
            word = "perro",
            translation = "dog",
            isQuestionToRemoveFromQueueAfterSuccess = false
        )

        manager.injectSessionStage(
            LearningSessionBackupState(
                currentQuestion = questionToLearn,
                learningStage = LearningStage.INITIAL_EVALUATION,
                isWordAQuestionInFlashcard = false,
                learningQueue = mutableListOf(questionToLearn),
                questionsCorrectInFirstStage = mutableListOf(),
                initialEvaluationMistakes = mutableListOf(),
                questionsToLearn = listOf(questionToLearn)
            )
        )

        manager.processUserAnswer(isAnswerCorrect = true)

        assertEquals(7, fakeCatRepo.incrementedCategoryId)
        assertEquals(42, fakeQuestionRepo.removedQuestionId)
    }
}
