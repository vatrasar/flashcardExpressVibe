package com.example.flashcardexpress.feature.repeat.domain.model

import android.R
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionToLearn(
    val id:Int,
    val word: String,
    val translation: String,
    var isQuestionToRemoveFromQueueAfterSuccess: Boolean,
    var numberOfMistakes:Int=0
) : Parcelable



