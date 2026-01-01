package com.example.flashcardexpress.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashcardexpress.core.data.local.dao.CategoryDao
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity

@Database(
    entities = [FlashcardEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FlashcardDb: RoomDatabase() {

    abstract fun flashcardDao(): CategoryDao
    companion object {

        const val DATABASE_NAME = "flashcard_db"
    }
}