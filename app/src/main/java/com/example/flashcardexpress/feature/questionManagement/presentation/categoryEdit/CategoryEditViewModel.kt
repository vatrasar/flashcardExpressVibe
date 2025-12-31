package com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.CategoryNameValidationUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.UpdateCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val updateCategoryUseCase: UpdateCategoryUseCase,
    val isCategoryNameValid: CategoryNameValidationUseCase

) :
    BaseScreenAndNavEffectsViewModel<CategoryEditEffect, CategoryEditNavEffect>() {

    private val args=savedStateHandle.toRoute<QuestionManagementScreen.CategoryEdit>()
    private val categoryId=args.categoryId
    private val _state = MutableStateFlow(CategoryEditState(args.categoryName))
    val state = _state.asStateFlow()
    public fun onEvent(event: CategoryEditEvent) {
        when (event) {
            CategoryEditEvent.OnBackToCategoryDetailsClicked -> sendNavEffect(CategoryEditNavEffect.BackToCategoryDetails)
            is CategoryEditEvent.OnCategoryNameChanged -> {
                _state.value = _state.value.copy(categoryName = event.currentValue)
            }
            CategoryEditEvent.OnSaveCategoryClicked -> {
                if(!isCategoryNameValid(_state.value.categoryName))
                {
                    sendEffect(CategoryEditEffect.ShowSnackbar("Category name need to have between 1 and 30 characters", SnackbarType.ERROR.label))
                    return
                }
                viewModelScope.launch {
                    launchCategoryUpdate()


                }
            }
        }

    }

    private suspend fun launchCategoryUpdate() {
        val result = updateCategoryUseCase(categoryId, _state.value.categoryName)
        if (result.isSuccess) {
            sendNavEffect(CategoryEditNavEffect.BackToCategoryDetailsAfterUpdate(_state.value.categoryName,categoryId))
        } else {
            val exception = result.exceptionOrNull()
            when (exception) {
                is FlashcardAppError.NameTakenError -> sendEffect(
                    CategoryEditEffect.ShowSnackbar(
                        "Error, category already exists!",
                        SnackbarType.ERROR.label
                    )
                )

                else -> sendEffect(
                    CategoryEditEffect.ShowSnackbar(
                        "Error, something went wrong!",
                        SnackbarType.ERROR.label
                    )
                )

            }

        }
    }
}