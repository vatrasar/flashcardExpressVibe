package com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import com.example.flashcardexpress.core.domain.util.UiText

sealed class CategoryEditEffect {
    data class ShowSnackbar(val message: UiText, val type: SnackbarType): CategoryEditEffect()
}

sealed class CategoryEditNavEffect {
    data object BackToCategoryDetails : CategoryEditNavEffect()
    data class BackToCategoryDetailsAfterUpdate(val categoryName:String,val categoryId:Int, val language:String) : CategoryEditNavEffect()

}

sealed class CategoryEditEvent {
    data class OnCategoryNameChanged(val currentValue: String) : CategoryEditEvent()
    data class OnLanguageChanged(val currentValue: String) : CategoryEditEvent()

    data object OnSaveCategoryClicked : CategoryEditEvent()
    data object OnBackToCategoryDetailsClicked : CategoryEditEvent()


}


@Immutable
data class CategoryEditState(
    val categoryName: String,
    val language: String,
    val languages: List<String> = emptyList()
)

