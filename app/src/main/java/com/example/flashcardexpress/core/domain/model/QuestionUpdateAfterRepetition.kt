package com.example.flashcardexpress.core.domain.model

data class QuestionUpdateAfterRepetition (
    val id:Int,val dateOfNextRepetition:Long,val  masterLevel:Int
    )