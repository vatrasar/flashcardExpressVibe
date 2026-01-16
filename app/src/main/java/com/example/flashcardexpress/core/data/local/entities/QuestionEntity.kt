package com.example.flashcardexpress.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(
    tableName = "flashcard",
    foreignKeys =[ ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuestionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    @ColumnInfo("category_id")
    val categoryId: Int,

    @ColumnInfo("word")
    val word: String,
    @ColumnInfo("translation")
    val translation: String,
    @ColumnInfo("learning_master_level")
    val learningMasterLevel: Int,
    @ColumnInfo("date_of_next_repetition")
    val dateOfNextRepetition: Long
)