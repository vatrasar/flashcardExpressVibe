package com.example.flashcardexpress.feature.questionManagement.domain.usecase

import com.example.flashcardexpress.core.domain.error.FlashcardAppError
import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.repository.FlashcardRepository
import javax.inject.Inject


class AddCategoryUseCase @Inject constructor(private val flashcardRepository: FlashcardRepository)  {
    suspend operator fun invoke(newCategoryName: String):Result<Unit>
    {
        if(flashcardRepository.isCategoryWithNameExists(newCategoryName))
        {
            return Result.failure(FlashcardAppError.NameTakenError())
        }

        try {
            flashcardRepository.insertCategory(newCategoryName)
            return Result.success(Unit)
        }catch (ex:Exception)
        {
            return Result.failure(ex)
        }
    }




}