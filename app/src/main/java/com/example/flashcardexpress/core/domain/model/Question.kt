package com.example.flashcardexpress.core.domain.model

import java.sql.Date
import java.time.LocalDate

data class Question(
    val word: String,
    val translation: String,
    val id: Int,
    val categoryId: Int,
    val learningMasterLevel: Int,
    val dateOfNextRepetition: LocalDate
)
