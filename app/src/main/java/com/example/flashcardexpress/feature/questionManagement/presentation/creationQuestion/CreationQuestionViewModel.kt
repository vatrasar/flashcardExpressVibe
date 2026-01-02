package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.lifecycle.viewModelScope
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAllCategoriesUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.AddQuestionUseCase
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.CategorySelectOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class CreationQuestionViewModel @Inject constructor(val addQuestion: AddQuestionUseCase, val getCategories: GetAllCategoriesUseCase) :
    BaseScreenAndNavEffectsViewModel<CreationQuestionEffect, CreationQuestionNavEffect>() {


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

    init {
        initStateWithCategoriesList()
    }

    fun initStateWithCategoriesList()
    {
        viewModelScope.launch {
            val categoriesResult=getCategories()
            categoriesResult.collect {
                val categorySelectOptionsList=it.map {
                    category->
                    CategorySelectOption(category.id,category.name)
                }
                _state.value=_state.value.copy(categories = categorySelectOptionsList)
            }

        }
    }

    private fun saveQuestion() {
        val formState = state.value
        if(formState.categorySelected==null)
        {
            sendEffect(CreationQuestionEffect.ShowSnackbar("Category is not selected!",
                SnackbarType.ERROR.label))
            return
        }
        val question = Question(formState.word, formState.translation, 1, formState.categorySelected.id)

        viewModelScope.launch {
            addQuestion(question)
            sendEffect(CreationQuestionEffect.ShowSnackbar("Flashcard created!", SnackbarType.SUCCESS.label))

        }
    }
}