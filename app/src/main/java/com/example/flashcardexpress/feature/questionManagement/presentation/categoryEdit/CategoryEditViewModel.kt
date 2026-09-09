package com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.CategoryNameValidationUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAvailableLanguagesUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.UpdateCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val updateCategoryUseCase: UpdateCategoryUseCase,
    val isCategoryNameValid: CategoryNameValidationUseCase,
    getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase
) :
    BaseScreenAndNavEffectsViewModel<CategoryEditEffect, CategoryEditNavEffect>() {

    private val args=savedStateHandle.toRoute<QuestionManagementScreen.CategoryEdit>()
    private val categoryId=args.categoryId
    private val _state = MutableStateFlow(
        CategoryEditState(
            categoryName = args.categoryName,
            language = args.language,
            languages = getAvailableLanguagesUseCase()
        )
    )
    val state = _state.asStateFlow()
    public fun onEvent(event: CategoryEditEvent) {
        when (event) {
            CategoryEditEvent.OnBackToCategoryDetailsClicked -> sendNavEffect(CategoryEditNavEffect.BackToCategoryDetails)
            is CategoryEditEvent.OnCategoryNameChanged -> {
                _state.value = _state.value.copy(categoryName = event.currentValue)
            }
            is CategoryEditEvent.OnLanguageChanged -> {
                _state.value = _state.value.copy(language = event.currentValue)
            }
            CategoryEditEvent.OnSaveCategoryClicked -> {
                if(!isCategoryNameValid(_state.value.categoryName))
                {
                    sendEffect(
                        CategoryEditEffect.ShowSnackbar(
                            com.example.flashcardexpress.core.domain.util.UiText.StringResource(
                                com.example.flashcardexpress.R.string.category_name_length_error
                            ),
                            SnackbarType.ERROR
                        )
                    )
                    return
                }
                viewModelScope.launch {
                    launchCategoryUpdate()


                }
            }
        }

    }

    private suspend fun launchCategoryUpdate() {
        val result = updateCategoryUseCase(categoryId, _state.value.categoryName, _state.value.language)
        if (result.isSuccess) {
            sendNavEffect(CategoryEditNavEffect.BackToCategoryDetailsAfterUpdate(_state.value.categoryName, categoryId, _state.value.language))
        } else {
            val exception = result.exceptionOrNull()
            when (exception) {
                is FlashcardAppError.NameTakenError -> sendEffect(
                    CategoryEditEffect.ShowSnackbar(
                        com.example.flashcardexpress.core.domain.util.UiText.StringResource(
                            com.example.flashcardexpress.R.string.category_exist_error
                        ),
                        SnackbarType.ERROR
                    )
                )

                else -> sendEffect(
                    CategoryEditEffect.ShowSnackbar(
                        com.example.flashcardexpress.core.domain.util.UiText.StringResource(
                            com.example.flashcardexpress.R.string.error_something_went_wrong
                        ),
                        SnackbarType.ERROR
                    )
                )

            }

        }
    }
}