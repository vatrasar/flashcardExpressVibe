package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import javax.inject.Inject


/**
 * Use case for adding a new flashcard category to the database.
 *
 * Validates that the category name is unique before insertion.
 *
 * Invoked by:
 * - [CreationCategoryViewModel]
 */
class AddCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository)  {
    suspend operator fun invoke(newCategoryName: String, language: String):Result<Unit>
    {
        if(categoryRepository.isCategoryWithNameExists(newCategoryName))
        {
            return Result.failure(FlashcardAppError.NameTakenError())
        }

        try {
            categoryRepository.insertCategory(newCategoryName, language)
            return Result.success(Unit)
        }catch (ex:Exception)
        {
            return Result.failure(ex)
        }
    }




}