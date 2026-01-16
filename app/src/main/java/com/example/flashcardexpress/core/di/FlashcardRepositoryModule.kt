package com.example.flashcardexpress.core.di

import com.example.flashcardexpress.core.data.repository.CategoryRepositoryImpl
import com.example.flashcardexpress.core.data.repository.QuestionRepositoryImpl
import com.example.flashcardexpress.core.data.repository.RepetitionRepositoryImpl
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FlashcardRepositoryModule {
    @Binds
   abstract fun bindFlashcardRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

   @Binds
   abstract fun bindQuestionRepository(questionRepositoryImpl: QuestionRepositoryImpl): QuestionRepository

   @Binds
   abstract fun bindRepetitionRepository(repetitionRepositoryImpl: RepetitionRepositoryImpl): RepetitionRepository

}