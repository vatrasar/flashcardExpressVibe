package com.example.flashcardexpress.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "category",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo("name")
    val name: String


)