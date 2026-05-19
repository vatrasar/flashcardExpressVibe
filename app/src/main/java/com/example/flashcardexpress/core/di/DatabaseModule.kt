package com.example.flashcardexpress.core.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flashcardexpress.core.data.local.FlashcardDb
import com.example.flashcardexpress.core.data.local.dao.CategoryDao
import com.example.flashcardexpress.core.data.local.dao.QuestionDao
import com.example.flashcardexpress.core.data.local.dao.RepetitionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE category ADD COLUMN language TEXT NOT NULL DEFAULT 'English'")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FlashcardDb {
        return databaseBuilder(
            context,
            FlashcardDb::class.java,
            FlashcardDb.DATABASE_NAME
        )
            .addMigrations(MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideFlashcardDao(database: FlashcardDb): CategoryDao {

        return database.flashcardDao()
    }

    @Provides
    fun provideQuestionDao(database: FlashcardDb): QuestionDao {
        return database.questionDao()
    }

    @Provides
    fun provideRepetitionDao(database: FlashcardDb): RepetitionDao {
        return database.repetitionDao()
    }

}