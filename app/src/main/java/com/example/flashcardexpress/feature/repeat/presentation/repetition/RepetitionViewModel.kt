package com.example.flashcardexpress.feature.repeat.presentation.repetition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.navigation.toRoute
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.data.androidTools.TTSManager

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.repeat.domain.RepetitionSessionManager
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard
import com.example.flashcardexpress.feature.repeat.domain.model.LearningSessionBackupState
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetQuestionsToLearnUseCase
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext


@HiltViewModel
class RepetitionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val getQuestionsToLearn: GetQuestionsToLearnUseCase,
    val repetitionSessionManager: RepetitionSessionManager,
    val categoryRepository: CategoryRepository,
    val ttsManager: TTSManager
) : BaseScreenAndNavEffectsViewModel<RepetitionEffect, RepetitionNavEffect>() {


    companion object{
        const val KEY_IS_NEW_LEARNING_SESSION="isNewLearningSession"
        const val KEY_SESSION_BACKUP_STATE = "session_backup_state"
    }
    private val _state = MutableStateFlow(RepetitionState(true,
        Flashcard("default question", "default translation"),0))
    val state = _state.asStateFlow()


    init {
        val args = savedStateHandle.toRoute<RepeatScreen.Repetition>()
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(args.categoryId)
            category?.let { cat ->
                _state.value = _state.value.copy(categoryLanguage = cat.language)
            }
        }
        if(args.isNewLearningSession) {
            initNewLearningSession(args)

        }
        else
            restoreLastLearningSessionAfterAndroidKilledProcess()


    }



    public fun onEvent(event: RepetitionEvent) {
        when (event) {
            RepetitionEvent.OnCancelRepetition -> sendNavEffect(RepetitionNavEffect.BackToRepeatPanel)
            RepetitionEvent.OnAnswerCorrect ->{
                onAnswerCorrect()


            }
            RepetitionEvent.OnAnswerIncorrect -> {
                onAnswerIncorrect()

            }
            RepetitionEvent.OnBackToQuestion ->
            {
                _state.value=_state.value.copy(isAnswerPage =false)
            }
            RepetitionEvent.OnShowAnswer -> {
                _state.value=_state.value.copy(isAnswerPage = true)
            }
            RepetitionEvent.OnPlayTtsClick -> {
                val text = if (_state.value.isAnswerPage) {
                    _state.value.flashcard.answer
                } else {
                    _state.value.flashcard.question
                }
                ttsManager.speak(text, _state.value.categoryLanguage)
            }

        }




    }

    private fun updateBackupSessionState() {

        savedStateHandle[KEY_SESSION_BACKUP_STATE] = this.repetitionSessionManager.getCurrentSesssionStateForBackup()


    }

    private fun restoreLastLearningSessionAfterAndroidKilledProcess() {
        val learningStateBackupSession = requireNotNull(savedStateHandle.get<LearningSessionBackupState>(KEY_SESSION_BACKUP_STATE)) {
            "LearningSessionBackupState is missing in SavedStateHandle! Did you forget to initialize it?"
        }

        repetitionSessionManager.injectSessionStage(learningStateBackupSession)
        var currentQuestion: Flashcard? = repetitionSessionManager.getCurrentFlashcard()
        val learningStageNumber = repetitionSessionManager.getCurrentStageNumber()
        initRepetitionState(currentQuestion,learningStageNumber)

    }

    private fun initNewLearningSession(args: RepeatScreen.Repetition) {
        viewModelScope.launch() {
            initSessionManagerState(args)
            savedStateHandle[KEY_IS_NEW_LEARNING_SESSION] = false
            updateBackupSessionState()
        }
    }

    private suspend fun initSessionManagerState(args: RepeatScreen.Repetition) {
        val categoryId = args.categoryId
        val numberOfQuestions = args.numberOfQuestionsInRepetition


        var currentQuestion: Flashcard? = null
        withContext(Dispatchers.IO) {
            val questionsToLearn = getQuestionsToLearn(categoryId, numberOfQuestions)
            repetitionSessionManager.injectQuestions(questionsToLearn)
            currentQuestion = repetitionSessionManager.getNextFlashcard()

        }

        val learningStageNumber = repetitionSessionManager.getCurrentStageNumber()


        initRepetitionState(currentQuestion, learningStageNumber)



    }

    private fun initRepetitionState(
        currentQuestion: Flashcard?,
        learningStageNumber: Int
    ) {
        if (currentQuestion == null) {
            sendNavEffect(RepetitionNavEffect.BackToRepeatPanel)
        } else {

            _state.value = RepetitionState(
                isAnswerPage = false,
                flashcard = currentQuestion,
                learningStage = learningStageNumber,
                categoryLanguage = _state.value.categoryLanguage
            )


        }
    }


    private fun updateStage() {
        updateScreenStage()
        updateBackupSessionState()

    }

    private fun updateScreenStage() {
        val stageNumber = repetitionSessionManager.getCurrentStageNumber()
        _state.value = _state.value.copy(learningStage = stageNumber)
    }

    private fun onAnswerIncorrect() {
        viewModelScope.launch {

            repetitionSessionManager.processUserAnswer(false)
            injectNextFlashcard()
            updateStage()

        }

    }

    private fun onAnswerCorrect() {
        viewModelScope.launch {
            repetitionSessionManager.processUserAnswer(true)
            injectNextFlashcard()
            updateStage()

        }


    }

    private suspend fun injectNextFlashcard() {


        val newFlahscard = repetitionSessionManager.getNextFlashcard()

        if (newFlahscard == null) {
            sendNavEffect(RepetitionNavEffect.BackToRepeatPanelAfterFinishingRepetition)

        } else {
            _state.value = _state.value.copy(isAnswerPage = false, flashcard = newFlahscard)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.stop()
    }
}