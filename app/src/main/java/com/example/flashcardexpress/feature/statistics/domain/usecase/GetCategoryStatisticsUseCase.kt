package com.example.flashcardexpress.feature.statistics.domain.usecase

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.statistics.domain.model.CategoryStatistics
import com.example.flashcardexpress.feature.statistics.domain.model.StageStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to retrieve statistics for a specific category.
 *
 * Invoked by:
 * - [com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryDetailViewModel]
 */
class GetCategoryStatisticsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val questionRepository: QuestionRepository
) {

    operator fun invoke(categoryId: Int): Flow<CategoryStatistics?> {
        return combine(
            categoryRepository.getCategoryFlowById(categoryId),
            questionRepository.getAllQuestionsOfCategory(categoryId)
        ) { category, questions ->
            buildCategoryStatistics(category, questions)
        }
    }

    private fun buildCategoryStatistics(
        category: Category?,
        questions: List<Question>
    ): CategoryStatistics? {
        if (category == null) {
            return null
        }
        val today = LocalDate.now()
        val learnedCount = category.learnedWordsCount
        val activeCount = questions.size
        val totalCount = learnedCount + activeCount
        val masteryPercentage = if (totalCount == 0) 0 else (learnedCount * 100) / totalCount
        val dueTodayCount = questions.count { !it.dateOfNextRepetition.isAfter(today) }
        val stages = buildStageStatistics(questions, learnedCount)

        return CategoryStatistics(
            categoryId = category.id,
            categoryName = category.name,
            language = category.language,
            learnedWordsCount = learnedCount,
            activeWordsCount = activeCount,
            totalWordsCount = totalCount,
            masteryPercentage = masteryPercentage,
            stageStatistics = stages,
            wordsReadyForTodayCount = dueTodayCount
        )
    }

    private fun buildStageStatistics(questions: List<Question>, masteredCount: Int): StageStatistics {
        var stage0 = 0
        var stage1 = 0
        var stage2 = 0
        var stage3 = 0
        var stage4 = 0

        for (question in questions) {
            when (question.learningMasterLevel) {
                0 -> stage0++
                1 -> stage1++
                2 -> stage2++
                3 -> stage3++
                4 -> stage4++
            }
        }

        return StageStatistics(
            stage0TodayOrNewCount = stage0,
            stage1DayCount = stage1,
            stage3DaysCount = stage2,
            stage10DaysCount = stage3,
            stage30DaysCount = stage4,
            masteredCount = masteredCount
        )
    }
}
