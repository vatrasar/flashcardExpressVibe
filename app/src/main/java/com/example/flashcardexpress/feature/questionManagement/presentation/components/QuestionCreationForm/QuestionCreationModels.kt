package com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm

import com.example.flashcardexpress.core.domain.model.Category

data class QuestionCreationFormActions(
    val onBack: () -> Unit,
    val onWordChanged: (String) -> Unit,
    val onTranslationChanged: (String) -> Unit,
    val onQuestionSave: () -> Unit,
    val onCategoryChanged: (CategorySelectOption) -> Unit

)

data class CategorySelectOption(val id: Int, val name: String)

data class QuestionCreationFormInputValues(
    val word: String,
    val translation:String,

)


