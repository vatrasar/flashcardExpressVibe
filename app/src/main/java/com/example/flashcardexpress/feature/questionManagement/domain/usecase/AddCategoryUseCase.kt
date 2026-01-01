package com.example.flashcardexpress.feature.questionManagement.domain.usecase

import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import javax.inject.Inject


class AddCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository)  {
    suspend operator fun invoke(newCategoryName: String):Result<Unit>
    {
        if(categoryRepository.isCategoryWithNameExists(newCategoryName))
        {
            return Result.failure(FlashcardAppError.NameTakenError())
        }

        try {
            categoryRepository.insertCategory(newCategoryName)
            return Result.success(Unit)
        }catch (ex:Exception)
        {
            return Result.failure(ex)
        }
    }




}