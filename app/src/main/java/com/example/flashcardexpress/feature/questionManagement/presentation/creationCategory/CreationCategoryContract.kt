package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.compose.runtime.Immutable

sealed class CreationCategoryEffect{
    data object NavigateBackToCreationMenu: CreationCategoryEffect()

}

sealed class CreationCategoryEvent{
    data class OnCategoryNameChanged(val currentValue:String): CreationCategoryEvent()
    data object OnAddCategoryClicked: CreationCategoryEvent()
    data object OnBackToCreationMenuClicked: CreationCategoryEvent()


}

@Immutable
data class CreationCategoryState(
    val categoryName: String
)