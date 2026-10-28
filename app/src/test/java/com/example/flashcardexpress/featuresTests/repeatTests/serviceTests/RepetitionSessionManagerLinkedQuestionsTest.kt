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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class RepetitionSessionManagerLinkedQuestionsTest {

    private class FakeCategoryRepository : CategoryRepository {
        override suspend fun insertCategory(categoryName: String, language: String) = Unit
        override suspend fun isCategoryWithNameExists(categoryName: String) = false
        override suspend fun isCategoryWithNameExistsExcludingId(categoryName: String, excludeCategoryId: Int) = false
        override fun getAllCategories(): Flow<List<Category>> = flowOf(emptyList())
        override suspend fun removeCategory(categoryId: Int) = Unit
        override suspend fun updateCategory(newCategory: Category) = Unit
        override suspend fun getCategoryById(categoryId: Int): Category? = null
        override fun getCategoryFlowById(categoryId: Int): Flow<Category?> = flowOf(null)
        override suspend fun incrementLearnedWordsCount(categoryId: Int) = Unit
        override fun getGlobalLearnedWordsCount(): Flow<Int> = flowOf(0)
    }

    private class FakeQuestionRepository(
        private val questions: List<Question> = emptyList(),
        private val linksMap: Map<Int, List<Int>> = emptyMap()
    ) : QuestionRepository {
        val updatedQuestions = mutableListOf<Question>()

        override suspend fun insertQuestion(question: Question) = Unit
        override fun getAllQuestionsOfCategory(categoryId: Int): Flow<List<Question>> = flowOf(questions)
        override suspend fun removeQuestion(questionId: Int) = Unit
        override suspend fun getQuestionById(questionId: Int) = questions.first { it.id == questionId }
        override suspend fun updateQuestion(question: Question) {
            updatedQuestions.add(question)
        }
        override fun getAllQuestions(): Flow<List<Question>> = flowOf(questions)
        override suspend fun getLinkedQuestionIds(questionId: Int): List<Int> = linksMap[questionId] ?: emptyList()
        override suspend fun getAllLinkedQuestionIds(questionId: Int): List<Int> = linksMap[questionId] ?: emptyList()
        override suspend fun linkQuestions(questionId: Int, targetQuestionId: Int) = Unit
        override suspend fun unlinkQuestions(questionId: Int, targetQuestionId: Int) = Unit
        override suspend fun getQuestionsByIds(questionIds: List<Int>): List<Question> =
            questions.filter { questionIds.contains(it.id) }
    }

    private class FakeRepetitionRepository : RepetitionRepository {
        val updatedQuestions = mutableListOf<QuestionUpdateAfterRepetition>()

        override fun getNumberOfQuestionsPerCategoryForToday() = flowOf(emptyList<com.example.flashcardexpress.core.domain.model.CategoryWithCount>())
        override suspend fun getQuestionsToLearn(categoryId: Int, numberOfQuestions: Int) = emptyList<Question>()
        override suspend fun updateQuestionAfterRepetition(question: QuestionUpdateAfterRepetition) {
            updatedQuestions.add(question)
        }
    }

    @Test
    fun processUserAnswer_whenAnswerWrongInInitialEvaluation_resetsProgressForLinkedQuestions() = runBlocking {
        val questionA = Question("hablar", "to speak", 1, 10, 2, LocalDate.now(), LocalDateTime.now())
        val questionB = Question("hallar", "to find", 2, 10, 3, LocalDate.now(), LocalDateTime.now())
        val fakeQuestionRepo = FakeQuestionRepository(
            questions = listOf(questionA, questionB),
            linksMap = mapOf(1 to listOf(2), 2 to listOf(1))
        )
        val fakeRepRepo = FakeRepetitionRepository()
        val fakeCatRepo = FakeCategoryRepository()

        val manager = RepetitionSessionManager(fakeRepRepo, fakeQuestionRepo, fakeCatRepo)
        val toLearnA = QuestionToLearn(1, "hablar", "to speak", false)
        val toLearnB = QuestionToLearn(2, "hallar", "to find", false)

        manager.injectSessionStage(
            LearningSessionBackupState(
                currentQuestion = toLearnA,
                learningStage = LearningStage.INITIAL_EVALUATION,
                isWordAQuestionInFlashcard = true,
                learningQueue = mutableListOf(toLearnA, toLearnB),
                questionsCorrectInFirstStage = mutableListOf(toLearnB),
                initialEvaluationMistakes = mutableListOf(),
                questionsToLearn = listOf(toLearnA, toLearnB)
            )
        )

        manager.processUserAnswer(isAnswerCorrect = false)

        val backup = manager.getCurrentSesssionStateForBackup()
        assertTrue(backup.initialEvaluationMistakes.any { it.id == 1 })
        assertTrue(backup.initialEvaluationMistakes.any { it.id == 2 })
        assertFalse(backup.questionsCorrectInFirstStage.any { it.id == 2 })
        assertTrue(fakeRepRepo.updatedQuestions.any { it.id == 1 && it.masterLevel == 0 })
        assertTrue(fakeRepRepo.updatedQuestions.any { it.id == 2 && it.masterLevel == 0 })
    }

    @Test
    fun processUserAnswer_whenAnswerWrongInLearningStage_movesAllLinkedQuestionsToEndOfQueue() = runBlocking {
        val questionA = Question("pero", "but", 1, 10, 1, LocalDate.now(), LocalDateTime.now())
        val questionB = Question("perro", "dog", 2, 10, 1, LocalDate.now(), LocalDateTime.now())
        val questionC = Question("gato", "cat", 3, 10, 1, LocalDate.now(), LocalDateTime.now())
        val fakeQuestionRepo = FakeQuestionRepository(
            questions = listOf(questionA, questionB, questionC),
            linksMap = mapOf(1 to listOf(2), 2 to listOf(1))
        )
        val fakeRepRepo = FakeRepetitionRepository()
        val fakeCatRepo = FakeCategoryRepository()

        val manager = RepetitionSessionManager(fakeRepRepo, fakeQuestionRepo, fakeCatRepo)
        val toLearnA = QuestionToLearn(1, "pero", "but", true)
        val toLearnB = QuestionToLearn(2, "perro", "dog", true)
        val toLearnC = QuestionToLearn(3, "gato", "cat", true)

        manager.injectSessionStage(
            LearningSessionBackupState(
                currentQuestion = toLearnA,
                learningStage = LearningStage.LEARNING,
                isWordAQuestionInFlashcard = true,
                learningQueue = mutableListOf(toLearnC, toLearnB),
                questionsCorrectInFirstStage = mutableListOf(),
                initialEvaluationMistakes = mutableListOf(),
                questionsToLearn = listOf(toLearnA, toLearnB, toLearnC)
            )
        )

        manager.processUserAnswer(isAnswerCorrect = false)

        val backup = manager.getCurrentSesssionStateForBackup()
        assertFalse(toLearnA.isQuestionToRemoveFromQueueAfterSuccess)
        assertFalse(toLearnB.isQuestionToRemoveFromQueueAfterSuccess)
        assertEquals(toLearnC.id, backup.learningQueue[0].id)
        assertTrue(backup.learningQueue.contains(toLearnA))
        assertTrue(backup.learningQueue.contains(toLearnB))
    }

    @Test
    fun syncLinkedQuestionsForCurrentQuestion_dynamicallyAddsNewlyLinkedQuestionsToSession() = runBlocking {
        val questionA = Question("canto", "singing", 1, 10, 1, LocalDate.now(), LocalDateTime.now())
        val questionB = Question("cuento", "story", 2, 10, 1, LocalDate.now(), LocalDateTime.now())
        val fakeQuestionRepo = FakeQuestionRepository(
            questions = listOf(questionA, questionB),
            linksMap = mapOf(1 to listOf(2))
        )
        val fakeRepRepo = FakeRepetitionRepository()
        val fakeCatRepo = FakeCategoryRepository()

        val manager = RepetitionSessionManager(fakeRepRepo, fakeQuestionRepo, fakeCatRepo)
        val toLearnA = QuestionToLearn(1, "canto", "singing", true)

        manager.injectSessionStage(
            LearningSessionBackupState(
                currentQuestion = toLearnA,
                learningStage = LearningStage.LEARNING,
                isWordAQuestionInFlashcard = true,
                learningQueue = mutableListOf(toLearnA),
                questionsCorrectInFirstStage = mutableListOf(),
                initialEvaluationMistakes = mutableListOf(),
                questionsToLearn = listOf(toLearnA)
            )
        )

        manager.syncLinkedQuestionsForCurrentQuestion()

        val backup = manager.getCurrentSesssionStateForBackup()
        assertEquals(2, backup.questionsToLearn.size)
        assertTrue(backup.questionsToLearn.any { it.id == 2 })
        assertTrue(backup.learningQueue.any { it.id == 2 })
    }
}
