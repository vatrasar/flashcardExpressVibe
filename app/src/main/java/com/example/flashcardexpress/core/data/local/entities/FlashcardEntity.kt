package com.example.flashcardexpress.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "flashcard",
    foreignKeys =[ ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FlashcardEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    @ColumnInfo("category_id")
    val categoryId: Int,

    @ColumnInfo("word")
    val word: String,
    @ColumnInfo("translation")
    val translation: String
)