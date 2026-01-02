package com.example.flashcardexpress.core.domain.model

data class Question(
    val word: String,
    val translation: String,
    val id: Int,
    val categoryId: Int
)
