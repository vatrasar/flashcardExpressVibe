package com.example.flashcardexpress.core.data.local.mapper

import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Question
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

val zone = ZoneId.systemDefault()

fun QuestionEntity.toDomain(): Question {
    return Question(
        word = word,
        translation = translation,
        id = id,
        categoryId = categoryId,
        learningMasterLevel = learningMasterLevel,
        dateOfNextRepetition = LocalDate.ofEpochDay(dateOfNextRepetition),
        createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), zone)
    )
}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        word = word,
        translation = translation,
        categoryId = categoryId,
        learningMasterLevel = learningMasterLevel,
        dateOfNextRepetition = dateOfNextRepetition.toEpochDay(),
        createdAt = createdAt.atZone(zone).toInstant().toEpochMilli()
    )
}

fun Question.toEntityWithNoId(): QuestionEntity {
    return QuestionEntity(
        word = word,
        translation = translation,
        categoryId = categoryId,
        learningMasterLevel = learningMasterLevel,
        dateOfNextRepetition = dateOfNextRepetition.toEpochDay(),
        createdAt = createdAt.atZone(zone).toInstant().toEpochMilli()
    )
}
