package com.example.flashcardexpress.feature.repeat.domain

import com.example.flashcardexpress.core.domain.model.QuestionUpdateAfterRepetition
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard
import com.example.flashcardexpress.feature.repeat.domain.model.enums.LearningStage
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.min

class RepetitionSessionManager @Inject constructor(val repetitionRepository: RepetitionRepository, val questionRepository: QuestionRepository) {


    private var learningStage: LearningStage =LearningStage.INITIAL_EVALUATION
    private var isWordAQuestionInFlashcard: Boolean=true
    private var questionsToLearn:List<QuestionToLearn> = listOf()
    private var initialEvaluationMistakes=mutableListOf<QuestionToLearn>()
    private var learningQueue=mutableListOf<QuestionToLearn>()
    private var currentQuestion: QuestionToLearn=QuestionToLearn(0,"default question","default translation",true)

    private var questionsCorrectInFirstStage=mutableListOf<QuestionToLearn>()

    fun injectQuestions(questionsToInject: List<QuestionToLearn>) {
        questionsToLearn=questionsToInject
        learningQueue=questionsToInject.toMutableList()
    }

    suspend fun getNextFlashcard(): Flashcard?
    {
        val nextQuestion=getNextQuestion()
        val isRepetitionFinished = nextQuestion == null
        if (isRepetitionFinished)
        {
            updateAllMistakesAfterFinishingRepetition()
            return null
        }
        currentQuestion = nextQuestion
        return buildFlashcard(nextQuestion)

    }




    suspend fun processUserAnswer(isAnswerCorrect: Boolean)
    {
        if (isAnswerCorrect)
        {
            processUserCorrectAnswer()
        }
        else{
            processUserWrongAnswer()
        }


    }

    fun getCurrentStageNumber():Int
    {
        var resultNumber=1
        when(learningStage)
        {
            LearningStage.LEARNING -> resultNumber+=2
            LearningStage.FINAL_TEST -> resultNumber+=3
            LearningStage.INITIAL_EVALUATION -> {}

        }
        if (!isWordAQuestionInFlashcard && learningStage!=LearningStage.INITIAL_EVALUATION)
        {
            resultNumber+=2
        }
        else if(learningStage==LearningStage.INITIAL_EVALUATION && !    isWordAQuestionInFlashcard)
        {
            resultNumber+=1
        }
        return resultNumber
    }

    private suspend fun updateAllMistakesAfterFinishingRepetition() {
        for (question in initialEvaluationMistakes) {
            updateInDbMasteryLevelAndNextRepetitionDate(0, question.id)
        }
    }



    private suspend fun processUserWrongAnswer() {
        when(learningStage)
        {
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

    private suspend fun processWrongAnswerDuringEvaluation() {
        initialEvaluationMistakes.add(currentQuestion)
        updateQuestionInDbAfterMistake()

    }


    private suspend fun updateQuestionInDbAfterMistake() {
        val today = LocalDate.now().toEpochDay()
        val questionUpdate = QuestionUpdateAfterRepetition(currentQuestion.id, today, 0)
        repetitionRepository.updateQuestionAfterRepetition(questionUpdate)
    }

    private fun processUserWrongAnswerDuringFinalTest() {
        currentQuestion.numberOfMistakes+=10
        learningQueue = questionsToLearn.shuffled().toMutableList()
        learningStage = LearningStage.LEARNING
    }

    private fun processUserWrongAnswerDuringLearning() {
        val indexToInjectQuestionInQueue: Int = min(learningQueue.size, 2)
        currentQuestion.isQuestionToRemoveFromQueueAfterSuccess = false
        currentQuestion.numberOfMistakes++
        learningQueue.add(indexToInjectQuestionInQueue, currentQuestion)
    }

    private suspend fun processUserCorrectAnswer()
    {
        if(learningStage== LearningStage.INITIAL_EVALUATION)
        {
            processUserCorrectAnswerDuringEvaluation()

        }
        processCorrectUserAnswerDuringLearning()
    }

    private fun processCorrectUserAnswerDuringLearning() {
        if (!currentQuestion.isQuestionToRemoveFromQueueAfterSuccess && learningQueue.size>2) {
            val index = min(learningQueue.size, 10)
            currentQuestion.isQuestionToRemoveFromQueueAfterSuccess = true
            learningQueue.add(index, currentQuestion)
        }
    }

    private suspend fun processUserCorrectAnswerDuringEvaluation() {

        if (isWordAQuestionInFlashcard) {
            questionsCorrectInFirstStage.add(currentQuestion)

        }
        else{
            val currentQuestionForDbOperation=currentQuestion.copy()
            val questionPreviousMasteryLevel=questionRepository.getQuestionById(currentQuestionForDbOperation.id).learningMasterLevel

            if (questionPreviousMasteryLevel>3)
            {
                questionRepository.removeQuestion(currentQuestionForDbOperation.id)
            }
            else{
                updateInDbMasteryLevelAndNextRepetitionDate(questionPreviousMasteryLevel,currentQuestionForDbOperation.id)
            }


        }

    }

    private suspend fun updateInDbMasteryLevelAndNextRepetitionDate(questionPreviousMasteryLevel: Int,questionId:Int) {
        var daysToNextRepetition: Int = 0
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

        var nextQuestion: QuestionToLearn?=null
        if(learningQueue.isEmpty()) {

            nextQuestion=getNextQuestionAfterFinishingLearningStage()
        }
        else{
            nextQuestion=learningQueue.removeAt(0)
        }
        return nextQuestion
    }

    private fun getNextQuestionAfterFinishingLearningStage(): QuestionToLearn?
    {
        val isRepetitionFinished = learningStage == LearningStage.FINAL_TEST && !isWordAQuestionInFlashcard
        if (isRepetitionFinished)
            return null
        updateStage()
        populateQueue()
        if (learningQueue.isEmpty())
        {
            return null
        }
        return learningQueue.removeAt(0)
    }





    private fun populateQueue() {
        if (learningStage== LearningStage.FINAL_TEST)
        {
            learningQueue=questionsToLearn.sortedBy { it.numberOfMistakes }.toMutableList()
        }
        else{
            learningQueue.addAll(questionsToLearn.shuffled())
        }

    }

    private fun updateStage()
    {

        when(learningStage) {
            LearningStage.LEARNING -> learningStage=LearningStage.FINAL_TEST
            LearningStage.FINAL_TEST -> {
                if (isWordAQuestionInFlashcard) {
                    isWordAQuestionInFlashcard = false
                    learningStage = LearningStage.LEARNING
                }


            }
            LearningStage.INITIAL_EVALUATION -> {

                val isAllQuestionWrongInFirstStage = initialEvaluationMistakes.size == questionsToLearn.size && isWordAQuestionInFlashcard
                if(isAllQuestionWrongInFirstStage)
                {
                    learningStage=LearningStage.LEARNING

                    return

                }


                if (isWordAQuestionInFlashcard) {
                    isWordAQuestionInFlashcard = false
                    questionsToLearn=questionsCorrectInFirstStage

                }
                else
                {
                    val isUserKnowAllWords = initialEvaluationMistakes.size == 0
                    if (isUserKnowAllWords)
                    {
                        learningStage=LearningStage.FINAL_TEST
                        isWordAQuestionInFlashcard=false
                        questionsToLearn=mutableListOf()
                        return

                    }
                    isWordAQuestionInFlashcard=true
                    learningStage=LearningStage.LEARNING
                    questionsToLearn=initialEvaluationMistakes
                }

            }


        }


    }










}