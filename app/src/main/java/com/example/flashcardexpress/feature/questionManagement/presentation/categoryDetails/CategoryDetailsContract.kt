package com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle

sealed class CategoryDetailsEffect {

}

sealed class CategoryDetailsNavEffect {
    data object BackToManagePanel: CategoryDetailsNavEffect()


}

sealed class CategoryDetailsEvent {
    data class OnQuestionRemoveClicked(val questionId: Int) : CategoryDetailsEvent()
    data class OnQuestionEditClicked(val questionId: Int) : CategoryDetailsEvent()
    data object OnBackToManagePanelClicked : CategoryDetailsEvent()
    data object OnDeleteCategoryClicked : CategoryDetailsEvent()
    data object OnEditCategoryClicked : CategoryDetailsEvent()

}


@Immutable
data class CategoryDetailsState(
    val questions:List<ElementForListWithTitle>,
    val categoryName: String

)