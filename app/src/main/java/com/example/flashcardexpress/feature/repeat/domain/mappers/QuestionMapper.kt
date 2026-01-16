package com.example.flashcardexpress.feature.repeat.domain.mappers

import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionToLearn
import java.time.LocalDate

fun Question.toQuestionToLearn(): QuestionToLearn {
    return QuestionToLearn(
        id,
        word,translation,true)
}

