package com.example.flashcardexpress.core.data.local

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.flashcardexpress.core.data.local.dao.FlashcardDao
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.FlashcardEntity

@Database(
    entities = [FlashcardEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FlashcardDb: RoomDatabase() {

    abstract fun flashcardDao(): FlashcardDao
    companion object {

        const val DATABASE_NAME = "flashcard_db"
    }
}