package com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAllCategoriesUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.RemoveCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.GetAllQuestionsOfCategoryUseCase
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.question.RemoveQuestionUseCase
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsNavEffect.*
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val removeCategoryUseCase: RemoveCategoryUseCase
    ,val getAllQuestionsOfCategory: GetAllQuestionsOfCategoryUseCase,
    val removeQuestion: RemoveQuestionUseCase
) : BaseScreenAndNavEffectsViewModel<CategoryDetailsEffect, CategoryDetailsNavEffect>(){

    val args = savedStateHandle.toRoute<QuestionManagementScreen.CategoryDetails>()
    val categoryId=args.categoryId
    val state =getAllQuestionsOfCategory(categoryId).map{

        CategoryDetailsState(it.map {
            ElementForListWithTitle(it.word, it.id)
        }.toList()
            ,args.categoryName)
    }.stateIn(
        scope = viewModelScope,
        initialValue = CategoryDetailsState(listOf(),args.categoryName),
        started =  SharingStarted.WhileSubscribed(5000)
    )








    public fun onEvent(event: CategoryDetailsEvent) {
        when (event) {
            is CategoryDetailsEvent.OnBackToManagePanelClicked -> {
                sendNavEffect(CategoryDetailsNavEffect.BackToManagePanel)
            }

            is CategoryDetailsEvent.OnQuestionEditClicked -> TODO()
            is CategoryDetailsEvent.OnQuestionRemoveClicked ->
            {
                viewModelScope.launch {
                    removeQuestion(event.questionId)
                }
            }
            CategoryDetailsEvent.OnDeleteCategoryClicked -> {
                viewModelScope.launch {
                    removeCategoryUseCase(categoryId)
                }
                sendNavEffect(CategoryDetailsNavEffect.BackToManagePanel)

            }
            CategoryDetailsEvent.OnEditCategoryClicked -> sendNavEffect(NavigateToCategoryEdit(categoryId,args.categoryName))
            CategoryDetailsEvent.OnCreateQuestionClicked -> sendNavEffect(NavigateToQuestionCreation(categoryId))

        }

    }
}