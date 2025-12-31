package com.example.flashcardexpress.feature.questionManagement.domain.usecase

import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.FlashcardRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val flashcardRepository: FlashcardRepository) {
    suspend operator fun invoke(categoryId: Int, newCategoryName: String):Result<Unit>
    {
        val category = Category(newCategoryName,categoryId)
        if(flashcardRepository.isCategoryWithNameExists(newCategoryName))
        {
            return Result.failure(FlashcardAppError.NameTakenError())
        }
        try {
            flashcardRepository.updateCategory(category)
        }catch (ex:Exception)
        {
            return Result.failure(ex)
        }
        return Result.success(Unit)


    }
}