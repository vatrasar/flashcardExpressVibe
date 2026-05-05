package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a stream of all flashcard categories.
 *
 * Invoked by:
 * - [ManagePanelViewModel]
 */
class GetAllCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {

    operator fun invoke(): Flow<List<Category>>
    {

       return repository.getAllCategories()

    }

}