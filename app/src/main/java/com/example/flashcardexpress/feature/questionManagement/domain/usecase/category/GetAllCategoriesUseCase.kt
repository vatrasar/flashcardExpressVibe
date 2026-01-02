package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {

    operator fun invoke(): Flow<List<Category>>
    {

       return repository.getAllCategories()

    }

}