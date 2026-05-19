package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.lifecycle.viewModelScope
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.AddCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.CategoryNameValidationUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAvailableLanguagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class CreationCategoryViewModel @Inject constructor(
    val addCategoryUseCase: AddCategoryUseCase,
    val isCategoryNameValid: CategoryNameValidationUseCase,
    getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase
) : BaseScreenAndNavEffectsViewModel<CreationCategoryEffect,CreationCategoryNavEffect>(){

    private val _state = MutableStateFlow(
        CreationCategoryState(
            categoryName = "",
            languages = getAvailableLanguagesUseCase()
        )
    )
    val state = _state.asStateFlow()

    public fun onEvent(event: CreationCategoryEvent)
    {
        when(event)
        {
            is CreationCategoryEvent.OnCategoryNameChanged -> updateStateAfterCategoryChanged(event)
            is CreationCategoryEvent.OnLanguageChanged -> updateStateAfterLanguageChanged(event)
            CreationCategoryEvent.OnAddCategoryClicked -> {
               addNewCategory()
            }
            CreationCategoryEvent.OnBackToManagePanel -> {
                sendNavEffect(CreationCategoryNavEffect.NavigateBackToManagePanel)
            }

        }

    }

    private fun addNewCategory() {
        val formState=state.value
        val newCategoryName=formState.categoryName
        val language=formState.language
        if(!isCategoryNameValid(newCategoryName))
        {
            sendEffect(CreationCategoryEffect.ShowSnackbar("Category name need to have between 1 and 30 characters", SnackbarType.ERROR.label))
            return
        }


        viewModelScope.launch {
            addCategoryLaunch(newCategoryName, language)

        }


    }



    private fun updateStateAfterCategoryChanged(event: CreationCategoryEvent.OnCategoryNameChanged)
    {
        _state.update { it.copy(categoryName = event.currentValue) }

    }

    private fun updateStateAfterLanguageChanged(event: CreationCategoryEvent.OnLanguageChanged)
    {
        _state.update { it.copy(language = event.currentValue) }

    }
    private suspend fun addCategoryLaunch(
        newCategoryName: String,
        language: String
    ) {
        val result = addCategoryUseCase(newCategoryName, language)
        if (result.isSuccess) {
            sendEffect(CreationCategoryEffect.ShowSnackbar("Category created!", SnackbarType.SUCCESS.label))


        } else  {
            val exception=result.exceptionOrNull()
            when(exception)
            {
                is FlashcardAppError.NameTakenError->sendEffect(CreationCategoryEffect.ShowSnackbar("Error, category already exists!", SnackbarType.ERROR.label))
                else->sendEffect(CreationCategoryEffect.ShowSnackbar("Error, something went wrong!", SnackbarType.ERROR.label))
            }


        }
    }


}

