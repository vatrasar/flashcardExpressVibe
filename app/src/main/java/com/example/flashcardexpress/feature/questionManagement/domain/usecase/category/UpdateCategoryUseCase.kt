package com.example.flashcardexpress.feature.questionManagement.domain.usecase.category

import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Int, newCategoryName: String):Result<Unit>
    {
        val category = Category(newCategoryName,categoryId)
        if(categoryRepository.isCategoryWithNameExists(newCategoryName))
        {
            return Result.failure(FlashcardAppError.NameTakenError())
        }
        try {
            categoryRepository.updateCategory(category)
        }catch (ex:Exception)
        {
            return Result.failure(ex)
        }
        return Result.success(Unit)


    }
}