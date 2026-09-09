package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.compose.runtime.Immutable

import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import com.example.flashcardexpress.core.domain.util.UiText

sealed class CreationCategoryNavEffect{
    data object NavigateBackToManagePanel: CreationCategoryNavEffect()

}

sealed class CreationCategoryEffect{
    data class ShowSnackbar(val message: UiText, val type: SnackbarType): CreationCategoryEffect()
}

sealed class CreationCategoryEvent{
    data class OnCategoryNameChanged(val currentValue:String): CreationCategoryEvent()
    data class OnLanguageChanged(val currentValue:String): CreationCategoryEvent()
    data object OnAddCategoryClicked: CreationCategoryEvent()
    data object OnBackToManagePanel: CreationCategoryEvent()


}

@Immutable
data class CreationCategoryState(
    val categoryName: String,
    val language: String = "English",
    val languages: List<String> = emptyList()
)