package com.example.flashcardexpress.feature.repeat.domain.model

import android.R

data class QuestionToLearn(
    val id:Int,
    val word: String,
    val translation: String,
    var isQuestionToRemoveFromQueueAfterSuccess: Boolean
)



