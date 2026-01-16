package com.example.flashcardexpress.core.di

import android.content.Context
import androidx.room.Room.databaseBuilder
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