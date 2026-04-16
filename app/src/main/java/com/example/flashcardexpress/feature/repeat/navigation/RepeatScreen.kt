package com.example.flashcardexpress.feature.repeat.navigation

import kotlinx.serialization.Serializable

sealed class RepeatScreen{

    @Serializable
    data class RepeatPanel(val isRepetitionFinished:Boolean): RepeatScreen()
    @Serializable
    data class Repetition(val categoryId:Int,val numberOfQuestionsInRepetition:Int,val isNewLearningSession:Boolean=true): RepeatScreen()




}