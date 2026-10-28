package com.example.flashcardexpress.feature.repeat.domain

import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.mappers.toQuestionToLearn
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard
import com.example.flashcardexpress.feature.repeat.domain.model.LearningSessionBackupState
import com.example.flashcardexpress.feature.repeat.domain.model.enums.LearningStage
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.min

/**
 * Manages the repetition session state, queues, and user answer evaluations.
 *
 * NOTE: Words that reach mastery level (> 3) are intentionally removed from the database
 * upon mastery as designed by the user (not a bug). Their completion is tracked by incrementing
 * the learned words count on their corresponding category.
 *
 * Invoked by:
 * - [RepetitionViewModel]
 */
class RepetitionSessionManager @Inject constructor(
    val repetitionRepository: RepetitionRepository,
    val questionRepository: QuestionRepository,
    val categoryRepository: CategoryRepository
) {

    private var learningStage: LearningStage = LearningStage.INITIAL_EVALUATION
    private var isWordAQuestionInFlashcard: Boolean = true
    private var questionsToLearn: List<QuestionToLearn> = listOf()
    private var initialEvaluationMistakes = mutableListOf<QuestionToLearn>()
    private var learningQueue = mutableListOf<QuestionToLearn>()
    private var currentQuestion: QuestionToLearn = QuestionToLearn(0, "default question", "default translation", true)
    private var questionsCorrectInFirstStage = mutableListOf<QuestionToLearn>()

    /**
     * Initializes the learning session with the provided questions.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun injectQuestions(questionsToInject: List<QuestionToLearn>) {
        questionsToLearn = questionsToInject
        learningQueue = questionsToInject.toMutableList()
    }

    /**
     * Restores a previously saved session state.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun injectSessionStage(stage: LearningSessionBackupState) {
        learningStage = stage.learningStage
        isWordAQuestionInFlashcard = stage.isWordAQuestionInFlashcard
        questionsToLearn = stage.questionsToLearn
        initialEvaluationMistakes = stage.initialEvaluationMistakes.toMutableList()
        learningQueue = stage.learningQueue.toMutableList()
        currentQuestion = stage.currentQuestion
    }

    /**
     * Retrieves the next flashcard in the learning session or null if the session is complete.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    suspend fun getNextFlashcard(): Flashcard? {
        val nextQuestion = getNextQuestion()
        val isRepetitionFinished = nextQuestion == null
        if (isRepetitionFinished) {
            updateAllMistakesAfterFinishingRepetition()
            return null
        }
        currentQuestion = nextQuestion
        return buildFlashcard(nextQuestion)
    }

    /**
     * Retrieves the flashcard for the current active question.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun getCurrentFlashcard(): Flashcard? {
        return buildFlashcard(currentQuestion)
    }

    /**
     * Creates a snapshot of the current session state for backup.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun getCurrentSesssionStateForBackup(): LearningSessionBackupState {
        return LearningSessionBackupState(
            currentQuestion = currentQuestion,
            learningStage = learningStage,
            isWordAQuestionInFlashcard = isWordAQuestionInFlashcard,
            learningQueue = learningQueue,
            questionsCorrectInFirstStage = questionsCorrectInFirstStage,
            initialEvaluationMistakes = initialEvaluationMistakes,
            questionsToLearn = questionsToLearn
        )
    }

    /**
     * Processes the user's answer (correct or incorrect) and advances session progress.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    suspend fun processUserAnswer(isAnswerCorrect: Boolean) {
        if (isAnswerCorrect) {
            processUserCorrectAnswer()
        } else {
            processUserWrongAnswer()
        }
    }

    /**
     * Computes the current step number to reflect in the UI progress bar.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun getCurrentStageNumber(): Int {
        var resultNumber = 1
        when (learningStage) {
            LearningStage.LEARNING -> resultNumber += 2
            LearningStage.FINAL_TEST -> resultNumber += 3
            LearningStage.INITIAL_EVALUATION -> {}
        }
        if (!isWordAQuestionInFlashcard && learningStage != LearningStage.INITIAL_EVALUATION) {
            resultNumber += 2
        } else if (learningStage == LearningStage.INITIAL_EVALUATION && !isWordAQuestionInFlashcard) {
            resultNumber += 1
        }
        return resultNumber
    }

    /**
     * Retrieves the identifier of the currently active question.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun getCurrentQuestionId(): Int = currentQuestion.id

    /**
     * Retrieves the word of the currently active question.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    fun getCurrentQuestionWord(): String = currentQuestion.word

    /**
     * Synchronizes any newly linked questions into the active session queue.
     *
     * Invoked by:
     * - [RepetitionViewModel]
     */
    suspend fun syncLinkedQuestionsForCurrentQuestion() {
        val linkedIds = questionRepository.getAllLinkedQuestionIds(currentQuestion.id)
        val missingIds = linkedIds.filter { id -> questionsToLearn.none { it.id == id } }
        if (missingIds.isEmpty()) return
        val extraQuestions = questionRepository.getQuestionsByIds(missingIds)
        val extraQuestionsToLearn = extraQuestions.map { it.toQuestionToLearn() }
        questionsToLearn = questionsToLearn + extraQuestionsToLearn
        learningQueue.addAll(extraQuestionsToLearn)
    }

    private suspend fun updateAllMistakesAfterFinishingRepetition() {
        for (question in initialEvaluationMistakes) {
            updateInDbMasteryLevelAndNextRepetitionDate(0, question.id)
        }
    }

    private suspend fun processUserWrongAnswer() {
        when (learningStage) {
            LearningStage.LEARNING -> {
                processUserWrongAnswerDuringLearning()
            }
            LearningStage.FINAL_TEST -> {
                processUserWrongAnswerDuringFinalTest()
            }
            LearningStage.INITIAL_EVALUATION -> {
                processWrongAnswerDuringEvaluation()
            }
        }
    }

    private suspend fun getLinkedQuestionsInSession(questionId: Int): List<QuestionToLearn> {
        val linkedIds = questionRepository.getAllLinkedQuestionIds(questionId).toSet()
        return questionsToLearn.filter { linkedIds.contains(it.id) }
    }

    private suspend fun processWrongAnswerDuringEvaluation() {
        initialEvaluationMistakes.add(currentQuestion)
        updateQuestionInDbAfterMistake(currentQuestion.id)
        val linkedQuestions = getLinkedQuestionsInSession(currentQuestion.id)
        for (linked in linkedQuestions) {
            handleLinkedMistakeInEvaluation(linked)
        }
    }

    private suspend fun handleLinkedMistakeInEvaluation(linkedQuestion: QuestionToLearn) {
        questionsCorrectInFirstStage.removeAll { it.id == linkedQuestion.id }
        if (!initialEvaluationMistakes.any { it.id == linkedQuestion.id }) {
            initialEvaluationMistakes.add(linkedQuestion)
        }
        updateQuestionInDbAfterMistake(linkedQuestion.id)
    }

    private suspend fun updateQuestionInDbAfterMistake(questionId: Int) {
        val today = LocalDate.now().toEpochDay()
        val questionUpdate = QuestionUpdateAfterRepetition(questionId, today, 0)
        repetitionRepository.updateQuestionAfterRepetition(questionUpdate)
    }

    private suspend fun processUserWrongAnswerDuringFinalTest() {
        currentQuestion.numberOfMistakes += 10
        val linkedQuestions = getLinkedQuestionsInSession(currentQuestion.id)
        for (linked in linkedQuestions) {
            linked.numberOfMistakes += 10
        }
        learningQueue = questionsToLearn.shuffled().toMutableList()
        learningStage = LearningStage.LEARNING
    }

    private suspend fun processUserWrongAnswerDuringLearning() {
        val linkedQuestions = getLinkedQuestionsInSession(currentQuestion.id)
        for (linked in linkedQuestions) {
            resetLinkedQuestionAndMoveToEndOfQueue(linked)
        }
        currentQuestion.isQuestionToRemoveFromQueueAfterSuccess = false
        currentQuestion.numberOfMistakes++
        learningQueue.remove(currentQuestion)
        learningQueue.add(currentQuestion)
    }

    private fun resetLinkedQuestionAndMoveToEndOfQueue(linkedQuestion: QuestionToLearn) {
        linkedQuestion.isQuestionToRemoveFromQueueAfterSuccess = false
        linkedQuestion.numberOfMistakes++
        learningQueue.remove(linkedQuestion)
        learningQueue.add(linkedQuestion)
    }

    private suspend fun processUserCorrectAnswer() {
        if (learningStage == LearningStage.INITIAL_EVALUATION) {
            processUserCorrectAnswerDuringEvaluation()
        }
        processCorrectUserAnswerDuringLearning()
    }

    private fun processCorrectUserAnswerDuringLearning() {
        if (!currentQuestion.isQuestionToRemoveFromQueueAfterSuccess && learningQueue.size > 2) {
            val index = min(learningQueue.size, 10)
            currentQuestion.isQuestionToRemoveFromQueueAfterSuccess = true
            learningQueue.add(index, currentQuestion)
        }
    }

    private suspend fun processUserCorrectAnswerDuringEvaluation() {
        if (isWordAQuestionInFlashcard) {
            questionsCorrectInFirstStage.add(currentQuestion)
        } else {
            val currentQuestionForDbOperation = currentQuestion.copy()
            val questionFromDb = questionRepository.getQuestionById(currentQuestionForDbOperation.id)
            val questionPreviousMasteryLevel = questionFromDb.learningMasterLevel

            if (questionPreviousMasteryLevel > 3) {
                handleMasteredQuestion(questionFromDb.categoryId, currentQuestionForDbOperation.id)
            } else {
                updateInDbMasteryLevelAndNextRepetitionDate(questionPreviousMasteryLevel, currentQuestionForDbOperation.id)
            }
        }
    }

    private suspend fun handleMasteredQuestion(categoryId: Int, questionId: Int) {
        categoryRepository.incrementLearnedWordsCount(categoryId)
        questionRepository.removeQuestion(questionId)
    }

    private suspend fun updateInDbMasteryLevelAndNextRepetitionDate(questionPreviousMasteryLevel: Int, questionId: Int) {
        var daysToNextRepetition = 0
        when (questionPreviousMasteryLevel) {
            0 -> daysToNextRepetition = 1
            1 -> daysToNextRepetition = 3
            2 -> daysToNextRepetition = 10
            3 -> daysToNextRepetition = 30
        }
        val dateOfNextRepetition =
            LocalDate.now().plusDays(daysToNextRepetition.toLong()).toEpochDay()
        val questionUpdate = QuestionUpdateAfterRepetition(
            questionId,
            dateOfNextRepetition,
            questionPreviousMasteryLevel + 1
        )
        repetitionRepository.updateQuestionAfterRepetition(questionUpdate)
    }

    private fun buildFlashcard(nextQuestion: QuestionToLearn): Flashcard {
        if (isWordAQuestionInFlashcard) {
            return Flashcard(nextQuestion.word, nextQuestion.translation)
        } else {
            return Flashcard(nextQuestion.translation, nextQuestion.word)
        }
    }

    private fun getNextQuestion(): QuestionToLearn? {
        var nextQuestion: QuestionToLearn? = null
        if (learningQueue.isEmpty()) {
            nextQuestion = getNextQuestionAfterFinishingLearningStage()
        } else {
            nextQuestion = learningQueue.removeAt(0)
        }
        return nextQuestion
    }

    private fun getNextQuestionAfterFinishingLearningStage(): QuestionToLearn? {
        val isRepetitionFinished = learningStage == LearningStage.FINAL_TEST && !isWordAQuestionInFlashcard
        if (isRepetitionFinished) return null
        updateStage()
        populateQueue()
        if (learningQueue.isEmpty()) {
            return null
        }
        return learningQueue.removeAt(0)
    }

    private fun populateQueue() {
        if (learningStage == LearningStage.FINAL_TEST) {
            learningQueue = questionsToLearn.sortedBy { it.numberOfMistakes }.toMutableList()
        } else {
            learningQueue.addAll(questionsToLearn.shuffled())
        }
    }

    private fun updateStage() {
        when (learningStage) {
            LearningStage.LEARNING -> learningStage = LearningStage.FINAL_TEST
            LearningStage.FINAL_TEST -> {
                if (isWordAQuestionInFlashcard) {
                    isWordAQuestionInFlashcard = false
                    learningStage = LearningStage.LEARNING
                }
            }
            LearningStage.INITIAL_EVALUATION -> {
                val isAllQuestionWrongInFirstStage = initialEvaluationMistakes.size == questionsToLearn.size && isWordAQuestionInFlashcard
                if (isAllQuestionWrongInFirstStage) {
                    learningStage = LearningStage.LEARNING
                    return
                }

                if (isWordAQuestionInFlashcard) {
                    isWordAQuestionInFlashcard = false
                    questionsToLearn = questionsCorrectInFirstStage
                } else {
                    val isUserKnowAllWords = initialEvaluationMistakes.size == 0
                    if (isUserKnowAllWords) {
                        learningStage = LearningStage.FINAL_TEST
                        isWordAQuestionInFlashcard = false
                        questionsToLearn = mutableListOf()
                        return
                    }
                    isWordAQuestionInFlashcard = true
                    learningStage = LearningStage.LEARNING
                    questionsToLearn = initialEvaluationMistakes
                }
            }
        }
    }
}
