package com.example.flashcardexpress.core.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FlashcardWithCategory (
    @Embedded
    val flashcard: QuestionEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)