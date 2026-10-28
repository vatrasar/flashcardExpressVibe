package com.example.flashcardexpress.feature.repeat.domain.model

import com.example.flashcardexpress.core.domain.model.Question

data class QuestionWithLinkStatus(
    val question: Question,
    val isLinked: Boolean
)
