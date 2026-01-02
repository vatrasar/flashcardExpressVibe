package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.CategorySelectOption

sealed class CreationQuestionEffect {
    data class ShowSnackbar(val message: String, val resultType: String) : CreationQuestionEffect()

}

sealed class CreationQuestionNavEffect {
    object NavigateBackToCreationMenu : CreationQuestionNavEffect()

}

sealed class CreationQuestionEvent {
    data class OnWordChanged(val word: String) : CreationQuestionEvent()
    data class OnTranslationChanged(val translation: String) : CreationQuestionEvent()
    object OnSaveQuestionClicked : CreationQuestionEvent()
    object OnBackToCreationMenuClicked : CreationQuestionEvent()
    data class OnCategoryChanged(val selectedCategory: CategorySelectOption) : CreationQuestionEvent()

}


@Immutable
data class CreationQuestionState(
    val word: String,
    val translation: String,
    val categorySelected: CategorySelectOption?,
    val categories: List<CategorySelectOption>
)



