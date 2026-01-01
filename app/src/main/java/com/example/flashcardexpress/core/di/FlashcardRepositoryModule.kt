package com.example.flashcardexpress.core.di

import com.example.flashcardexpress.core.data.repository.CategoryRepositoryImpl
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FlashcardRepositoryModule {
    @Binds
   abstract fun bindFlashcardRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository
}