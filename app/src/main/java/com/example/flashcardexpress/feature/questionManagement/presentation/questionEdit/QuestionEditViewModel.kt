package com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit

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
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.AddQuestionUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.GetQuestionByIdUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.UpdateQuestionUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class QuestionEditViewModel @Inject constructor(savedStateHandle: SavedStateHandle,
                                                private val getQuestionByIdUseCase: GetQuestionByIdUseCase,private val updateQuestion: UpdateQuestionUseCase
) :
    BaseScreenAndNavEffectsViewModel<QuestionEditEffect, QuestionEditNavEffect>() {
    private val args=savedStateHandle.toRoute<QuestionManagementScreen.QuestionEdit>()
    private var categoryId=0
    private val _state = MutableStateFlow(QuestionEditState(
        word = "",
        translation = ""

    ))


    init {

        viewModelScope.launch {
            val question=getQuestionByIdUseCase(args.questionId)
            categoryId=question.categoryId

            _state.value=state.value.copy(
                word = question.word,
                translation = question.translation
            )
        }



    }
    val state = _state.asStateFlow()
    public fun onEvent(event: QuestionEditEvent) {
        when (event) {
            QuestionEditEvent.OnBackToCategoryDetailsClicked -> {
                sendNavEffect(QuestionEditNavEffect.BackToCategoryDetails)
            }
            QuestionEditEvent.OnSaveQuestionClicked -> {
                saveNewVersionOfQuestion()

            }
            is QuestionEditEvent.OnTranslationChanged -> {
                _state.value = state.value.copy(translation = event.translation)
            }
            is QuestionEditEvent.OnWordChanged -> {
                _state.value = state.value.copy(word = event.word)
            }
        }

    }

    private fun saveNewVersionOfQuestion() {
        val stateValue = state.value
        val questionNewVersion =
            Question(stateValue.word, stateValue.translation, args.questionId, categoryId)

        viewModelScope.launch {
            updateQuestion(questionNewVersion)
            sendNavEffect(QuestionEditNavEffect.BackToCategoryDetails)

        }
    }
}