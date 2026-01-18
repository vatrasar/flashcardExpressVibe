package com.example.flashcardexpress.feature.repeat.presentation.repetition

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.repeat.domain.RepetitionSessionManager
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetQuestionsToLearnUseCase
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.descriptors.setSerialDescriptor


@HiltViewModel
class RepetitionViewModel @Inject constructor(val savedStateHandle: SavedStateHandle,val getQuestionsToLearn: GetQuestionsToLearnUseCase,val repetitionSessionManager: RepetitionSessionManager) :
    BaseScreenAndNavEffectsViewModel<RepetitionEffect, RepetitionNavEffect>() {



    private val _state = MutableStateFlow(RepetitionState(true,
        Flashcard("default question", "default translation"),0))
    val state = _state.asStateFlow()


    init {
        val args=savedStateHandle.toRoute<RepeatScreen.Repetition>()
        val categoryId=args.categoryId
        val numberOfQuestions=args.numberOfQuestionsInRepetition
        viewModelScope.launch() {

            var currentQuestion: Flashcard?=null
            withContext(Dispatchers.IO){
                val questionsToLearn=getQuestionsToLearn(categoryId,numberOfQuestions)
                repetitionSessionManager.injectQuestions(questionsToLearn)
                currentQuestion=repetitionSessionManager.getNextFlashcard()

            }

            val learningStageNumber=repetitionSessionManager.getCurrentStageNumber()


            if (currentQuestion==null)
            {
                sendNavEffect(RepetitionNavEffect.BackToRepeatPanel)
            }
            else{

              _state.value=RepetitionState(false,currentQuestion,learningStageNumber)


            }


        }

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

        }




    }

    private fun updateStage() {
        val stageNumber = repetitionSessionManager.getCurrentStageNumber()
        _state.value = _state.value.copy(learningStage = stageNumber)
    }

    private fun onAnswerIncorrect() {
        viewModelScope.launch {

                repetitionSessionManager.processUserAnswer(false)


        }
        injectNextFlashcard()
        updateStage()
    }

    private fun onAnswerCorrect() {
        viewModelScope.launch {
            try {
                repetitionSessionManager.processUserAnswer(true)
            }catch (e: Exception)
            {
                Log.d("error",e.message.toString())
            }

        }
        injectNextFlashcard()
        updateStage()

    }

    private fun injectNextFlashcard() {

        viewModelScope.launch {
            val newFlahscard = repetitionSessionManager.getNextFlashcard()

            if (newFlahscard == null) {
                sendNavEffect(RepetitionNavEffect.BackToRepeatPanelAfterFinishingRepetition)

            } else {
                _state.value = _state.value.copy(isAnswerPage = false, flashcard = newFlahscard)
            }
        }


    }
}