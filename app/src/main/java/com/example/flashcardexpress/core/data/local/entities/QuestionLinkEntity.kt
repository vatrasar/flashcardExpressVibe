package com.example.flashcardexpress.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "question_links",
    primaryKeys = ["question_id", "linked_question_id"],
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["linked_question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("question_id"),
        Index("linked_question_id")
    ]
)
data class QuestionLinkEntity(
    @ColumnInfo("question_id")
    val questionId: Int,

    @ColumnInfo("linked_question_id")
    val linkedQuestionId: Int
)
