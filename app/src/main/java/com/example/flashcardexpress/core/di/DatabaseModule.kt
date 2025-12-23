package com.example.flashcardexpress.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.flashcardexpress.core.data.local.FlashcardDb
import com.example.flashcardexpress.core.data.local.dao.FlashcardDao
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
        ).build()
    }
    @Provides
    fun provideFlashcardDao(database: FlashcardDb): FlashcardDao {
        // Hilt bierze bazę z funkcji powyżej i wyciąga z niej DAO
        return database.flashcardDao()
    }
}