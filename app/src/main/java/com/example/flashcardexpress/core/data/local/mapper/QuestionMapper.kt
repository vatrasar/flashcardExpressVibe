package com.example.flashcardexpress.core.data.local.mapper

import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Question
import java.time.Instant
import java.time.ZoneId

val zone= ZoneId.systemDefault()

fun QuestionEntity.toDomain(): Question
{
    return Question(
        word,translation,id,categoryId,learningMasterLevel,
        Instant.ofEpochMilli(dateOfNextRepetition)
            .atZone(zone)
            .toLocalDate()
        )

}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        word = word,
        translation = translation,
        categoryId = categoryId,
        learningMasterLevel = learningMasterLevel,
        dateOfNextRepetition =dateOfNextRepetition
            .toEpochDay()

    )
}

fun Question.toEntityWithNoId(): QuestionEntity {
    return QuestionEntity(

        word = word,
        translation = translation,
        categoryId = categoryId,
        learningMasterLevel = learningMasterLevel,
        dateOfNextRepetition =dateOfNextRepetition
            .toEpochDay()

    )
}


