package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAllCategoriesUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.AddQuestionUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.CategorySelectOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime


@HiltViewModel
class CreationQuestionViewModel @Inject constructor(val addQuestion: AddQuestionUseCase, val savedStateHandle: SavedStateHandle) :
    BaseScreenAndNavEffectsViewModel<CreationQuestionEffect, CreationQuestionNavEffect>() {


    val args = savedStateHandle.toRoute<QuestionManagementScreen.CreationQuestion>()
    val categoryId=args.categoryId
    private val _state = MutableStateFlow(CreationQuestionState(
        "","",null, emptyList()
    ))
    val state = _state.asStateFlow()
    public fun onEvent(event: CreationQuestionEvent) {
        when (event) {
            CreationQuestionEvent.OnBackToCreationMenuClicked -> {
                sendNavEffect(CreationQuestionNavEffect.NavigateBackToCreationMenu)
            }
            CreationQuestionEvent.OnSaveQuestionClicked -> {
                saveQuestion()

            }
            is CreationQuestionEvent.OnTranslationChanged -> {
                _state.value = _state.value.copy(translation = event.translation)
            }
            is CreationQuestionEvent.OnWordChanged -> {
                _state.value = _state.value.copy(word = event.word)

            }

            is CreationQuestionEvent.OnCategoryChanged -> {
                _state.value=_state.value.copy(categorySelected = event.selectedCategory)
            }
        }

    }





    private fun saveQuestion() {
        val formState = state.value

        val question = Question(
            word = formState.word,
            translation = formState.translation,
            id = 0,
            categoryId = categoryId,
            learningMasterLevel = 0,
            dateOfNextRepetition = LocalDate.now(),
            createdAt = LocalDateTime.now()
        )

        viewModelScope.launch {
            addQuestion(question)
            sendEffect(CreationQuestionEffect.ShowSnackbar("Flashcard created!", SnackbarType.SUCCESS.label))
            _state.value=_state.value.copy(
                word = "",
                translation = ""
            )
        }
    }
}