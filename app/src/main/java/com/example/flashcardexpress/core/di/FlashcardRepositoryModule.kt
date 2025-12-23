package com.example.flashcardexpress.core.di

import com.example.flashcardexpress.core.data.repository.FlashcardRepositoryImpl
import com.example.flashcardexpress.core.domain.repository.FlashcardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FlashcardRepositoryModule {

    @Binds
   abstract fun bindFlashcardRepository(flashcardRepositoryImpl: FlashcardRepositoryImpl): FlashcardRepository
}