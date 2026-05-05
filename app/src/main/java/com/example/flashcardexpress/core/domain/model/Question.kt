package com.example.flashcardexpress.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Question(
    val word: String,
    val translation: String,
    val id: Int,
    val categoryId: Int,
    val learningMasterLevel: Int,
    val dateOfNextRepetition: LocalDate,
    val createdAt: LocalDateTime
)
