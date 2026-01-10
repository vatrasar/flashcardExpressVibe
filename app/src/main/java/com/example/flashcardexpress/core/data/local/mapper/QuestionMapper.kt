package com.example.flashcardexpress.core.data.local.mapper

import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.domain.model.Question

fun QuestionEntity.toDomain(): Question
{
    return Question(word,translation,id,categoryId)

}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        word = word,
        translation = translation,
        categoryId = categoryId
    )
}