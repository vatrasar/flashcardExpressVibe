package com.example.flashcardexpress.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashcardexpress.core.data.local.dao.CategoryDao
import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.dao.QuestionLinkDao
import com.example.flashcardexpress.core.data.local.dao.RepetitionDao
import com.example.flashcardexpress.core.data.local.entities.CategoryEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionEntity
import com.example.flashcardexpress.core.data.local.entities.QuestionLinkEntity

@Database(
    entities = [QuestionEntity::class, CategoryEntity::class, QuestionLinkEntity::class],
    version = 8,
    exportSchema = false
)
abstract class FlashcardDb: RoomDatabase() {

    abstract fun flashcardDao(): CategoryDao
    abstract fun questionDao(): QuestionDao
    abstract fun repetitionDao(): RepetitionDao
    abstract fun questionLinkDao(): QuestionLinkDao


    companion object {

        const val DATABASE_NAME = "flashcard_db"
    }
}