package com.example.flashcardexpress.feature.questionManagement.domain.usecase

import android.util.Log
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import javax.inject.Inject

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