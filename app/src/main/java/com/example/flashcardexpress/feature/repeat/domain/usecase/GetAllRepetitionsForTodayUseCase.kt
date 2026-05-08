package com.example.flashcardexpress.feature.repeat.domain.usecase

import com.example.flashcardexpress.core.domain.model.CategoryWithCount
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.core.domain.repository.RepetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for retrieving all flashcard repetitions scheduled for the current day.
 *
 * This use case handles splitting large repetition sessions into smaller, more manageable chunks (sessions).
 *
 * Invoked by:
 * - [RepeatPanelViewModel]
 */
class GetAllRepetitionsForTodayUseCase @Inject constructor(private val repetitionRepository: RepetitionRepository) {

    operator fun invoke():Flow<List<CategoryWithCount>>
    {
        val categoryWithCountList: Flow<List<CategoryWithCount>> = repetitionRepository.getNumberOfQuestionsPerCategoryForToday()

        return categoryWithCountList.map { list->
            list.flatMap {
                splitCategoryIfTooBig(it)
            }

        }


    }

    private fun splitCategoryIfTooBig(categoryWithCount: CategoryWithCount): List<CategoryWithCount> {
        val maxQuestionsPerSession = 25
        val splitThreshold = 15
        var orginalCategoryWithCount=categoryWithCount
        var resultList=listOf<CategoryWithCount>()


        while(orginalCategoryWithCount.count>=maxQuestionsPerSession)
        {
            val countOfNewCategoryWithCount=splitThreshold
            val countOfOrginalCategoryWithCount=orginalCategoryWithCount.count-splitThreshold
            orginalCategoryWithCount=orginalCategoryWithCount.copy(count=countOfOrginalCategoryWithCount)
            val newCategoryWithCount=orginalCategoryWithCount.copy(count=countOfNewCategoryWithCount)
            resultList=resultList.plus(newCategoryWithCount)
        }
        resultList=resultList.plus(orginalCategoryWithCount)
        return resultList

    }



}