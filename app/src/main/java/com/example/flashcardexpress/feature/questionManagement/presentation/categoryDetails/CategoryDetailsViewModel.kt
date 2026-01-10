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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    val _isConfirmCategoryDeleteDialogVisible= MutableStateFlow(false);
    val _isConfirmQuestionDeleteDialogVisible= MutableStateFlow(false);
    var idOfQuestionToDelete= 0;
    val state = buildState()




    fun onEvent(event: CategoryDetailsEvent) {
        when (event) {
            is CategoryDetailsEvent.OnBackToManagePanelClicked -> {
                sendNavEffect(BackToManagePanel)
            }

            is CategoryDetailsEvent.OnQuestionEditClicked -> {
                sendNavEffect(NavigateToQuestionEdit(event.questionId))
            }
            is CategoryDetailsEvent.OnQuestionRemoveClicked ->
            {
                _isConfirmQuestionDeleteDialogVisible.value=true
                idOfQuestionToDelete=event.questionId

            }
            CategoryDetailsEvent.OnDeleteCategoryClicked -> {

                _isConfirmCategoryDeleteDialogVisible.value=true

            }
            CategoryDetailsEvent.OnEditCategoryClicked -> sendNavEffect(NavigateToCategoryEdit(categoryId,args.categoryName))
            CategoryDetailsEvent.OnCreateQuestionClicked -> sendNavEffect(NavigateToQuestionCreation(categoryId))
            CategoryDetailsEvent.OnAlertDismissClicked -> closeAlertWindow()
            CategoryDetailsEvent.OnConfirmDeleteCategoryClicked ->{
                removeCategory()
            }

            CategoryDetailsEvent.OnConfirmDeleteQuestionClicked -> {
                removeQuestion()
            }
        }

    }

    private fun removeQuestion() {
        viewModelScope.launch {
            removeQuestion(idOfQuestionToDelete)
        }
    }

    private fun removeCategory() {
        viewModelScope.launch {
            removeCategoryUseCase(categoryId)
        }
        sendNavEffect(BackToManagePanel)
    }

    fun closeAlertWindow()
    {
        _isConfirmCategoryDeleteDialogVisible.value=false
        _isConfirmQuestionDeleteDialogVisible.value=false

    }

    private fun buildState(): StateFlow<CategoryDetailsState> = combine(
        getAllQuestionsOfCategory(categoryId),
        _isConfirmCategoryDeleteDialogVisible,
        _isConfirmQuestionDeleteDialogVisible

    ) { questions, isConfirmCategoryDeleteDialogVisible, isConfirmQuestionDeleteDialogVisible ->
        val elementsOnListWithTitle = questions.map {
            ElementForListWithTitle(it.word, it.id)
        }.toList()

        CategoryDetailsState(
            elementsOnListWithTitle,
            args.categoryName,
            isConfirmCategoryDeleteDialogVisible,
            isConfirmQuestionDeleteDialogVisible
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = CategoryDetailsState(listOf(), args.categoryName, false, false),
        started = SharingStarted.WhileSubscribed(5000)
    )
}