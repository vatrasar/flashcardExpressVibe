package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import javax.inject.Inject

/**
 * Use case for validating a category name.
 *
 * Checks if the name is between 1 and 30 characters long and contains non-whitespace characters.
 *
 * Invoked by:
 * - [CreationCategoryViewModel]
 * - [CategoryEditViewModel]
 */
class CategoryNameValidationUseCase @Inject constructor()  {
    operator fun invoke(name:String): Boolean {
        val stringNoWhiteSigns:String=name.filterNot { it.isWhitespace() }
        if(stringNoWhiteSigns.length !in (1..30) || name.length !in (1..30))
        {
            return false
        }
        return true
    }
}