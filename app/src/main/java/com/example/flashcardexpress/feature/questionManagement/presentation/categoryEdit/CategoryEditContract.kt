package com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryEffect

sealed class CategoryEditEffect {
    data class ShowSnackbar(val message:String,val resultType:String):CategoryEditEffect()

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
    val language: String
)

