package com.example.flashcardexpress.feature.questionManagement.presentation.managePanel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.feature.questionManagement.domain.usecase.category.GetAllCategoriesUseCase
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelNavEffect.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class ManagePanelViewModel @Inject constructor(val getAllCategoriesUseCase: GetAllCategoriesUseCase) : BaseScreenAndNavEffectsViewModel<ManagePanelEffect, ManagePanelNavEffect>() {


    val state: StateFlow<ManagePanelState> = getAllCategoriesUseCase().map { categories ->
        ManagePanelState(convertCategoriesToElementsOfListWithTitle(categories))
    }.stateIn(
        scope = viewModelScope,
        initialValue = ManagePanelState(listOf()),
        started =  SharingStarted.WhileSubscribed(5000)
    )
    public fun onEvent(event: ManagePanelEvent) {
        when (event) {
            is ManagePanelEvent.OnCategoryClicked -> {
                val categoryElement=event.category
                val category=Category(categoryElement.elementName,categoryElement.id)
                sendNavEffect(NavigateCategoryDetails(category))
            }

            ManagePanelEvent.OnBackToHomeClicked -> {
                sendNavEffect(BackToHome)
            }

            ManagePanelEvent.OnNavigateToCategoryCreation -> {
                sendNavEffect(NavigateToCategoryCreation)
            }

        }
    }





}

private fun convertCategoriesToElementsOfListWithTitle(categories: List<Category>): List<ElementForListWithTitle> =
    categories.map { ElementForListWithTitle(it.name, it.id) }