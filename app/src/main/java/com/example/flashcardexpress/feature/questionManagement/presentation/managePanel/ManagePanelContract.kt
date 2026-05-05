package com.example.flashcardexpress.feature.questionManagement.presentation.managePanel

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.core.domain.model.Category

sealed class ManagePanelEffect {

}

sealed class ManagePanelNavEffect{
    data class NavigateCategoryDetails(val category: Category) : ManagePanelNavEffect()
    data object NavigateToCategoryCreation : ManagePanelNavEffect()

}

sealed class ManagePanelEvent {
    data class OnCategoryClicked(val category: ElementForListWithTitle) : ManagePanelEvent()
    data object OnNavigateToCategoryCreation : ManagePanelEvent()

}

@Immutable
data class ManagePanelState(
    val listOfCategories: List<ElementForListWithTitle>
)