package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.compose.runtime.Immutable

sealed class CreationCategoryNavEffect{
    data object NavigateBackToCreationMenuNav: CreationCategoryNavEffect()

}

sealed class CreationCategoryEffect{
    data class ShowSnackbar(val message:String,val resultType:String):CreationCategoryEffect()

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