package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import javax.inject.Inject

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