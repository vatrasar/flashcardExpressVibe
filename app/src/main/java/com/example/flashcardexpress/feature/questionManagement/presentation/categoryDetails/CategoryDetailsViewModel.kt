package com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.RemoveCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val removeCategoryUseCase: RemoveCategoryUseCase
) : BaseScreenAndNavEffectsViewModel<CategoryDetailsEffect, CategoryDetailsNavEffect>(){

    val args = savedStateHandle.toRoute<QuestionManagementScreen.CategoryDetails>()
    private val _state = MutableStateFlow(CategoryDetailsState(listOf(),args.categoryName))
    val categoryId=args.categoryId
    val state = _state.asStateFlow()




    public fun onEvent(event: CategoryDetailsEvent) {
        when (event) {
            is CategoryDetailsEvent.OnBackToManagePanelClicked -> {
                sendNavEffect(CategoryDetailsNavEffect.BackToManagePanel)
            }

            is CategoryDetailsEvent.OnQuestionEditClicked -> TODO()
            is CategoryDetailsEvent.OnQuestionRemoveClicked -> TODO()
            CategoryDetailsEvent.OnDeleteCategoryClicked -> {
                viewModelScope.launch {
                    removeCategoryUseCase(categoryId)
                }
                sendNavEffect(CategoryDetailsNavEffect.BackToManagePanel)

            }
            CategoryDetailsEvent.OnEditCategoryClicked -> sendNavEffect(CategoryDetailsNavEffect.NavigateToCategoryEdit(categoryId,args.categoryName))
        }

    }
}