package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.CategorySelectOption

import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import com.example.flashcardexpress.core.domain.util.UiText

sealed class CreationQuestionEffect {
    data class ShowSnackbar(val message: UiText, val type: SnackbarType) : CreationQuestionEffect()
}

sealed class CreationQuestionNavEffect {
    data object NavigateBackToCreationMenu : CreationQuestionNavEffect()
}

sealed class CreationQuestionEvent {
    data class OnWordChanged(val word: String) : CreationQuestionEvent()
    data class OnTranslationChanged(val translation: String) : CreationQuestionEvent()
    data object OnSaveQuestionClicked : CreationQuestionEvent()
    data object OnBackToCreationMenuClicked : CreationQuestionEvent()
    data class OnCategoryChanged(val selectedCategory: CategorySelectOption) : CreationQuestionEvent()
}


@Immutable
data class CreationQuestionState(
    val word: String,
    val translation: String,
    val categorySelected: CategorySelectOption?,
    val categories: List<CategorySelectOption>
)



