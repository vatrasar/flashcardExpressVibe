package com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm

data class CategoryFormActions (
    val onCategoryNameChanged: (String) -> Unit,
    val onCategorySave: () -> Unit,
    val onBack: () -> Unit
)

