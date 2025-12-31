package com.example.flashcardexpress.feature.questionManagement.domain.usecase

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: FlashcardRepository) {

    operator fun invoke(): Flow<List<Category>>
    {

       return repository.getAllCategories()

    }

}