package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import android.util.Log
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * Use case for deleting a flashcard category and all its associated questions from the database.
 *
 * Invoked by:
 * - [ManagePanelViewModel]
 * - [CategoryDetailsViewModel]
 */
class RemoveCategoryUseCase @Inject constructor(private val repository: CategoryRepository) {

    suspend operator fun invoke(categoryId: Int)
    {
        try {
            repository.removeCategory(categoryId)
        }
        catch (ex:Exception)
        {
            Log.e("RemoveCategoryUseCase", "invoke: ", ex)


        }




    }
}