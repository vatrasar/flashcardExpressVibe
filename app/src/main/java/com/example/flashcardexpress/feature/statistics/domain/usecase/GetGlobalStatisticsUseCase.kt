package com.example.flashcardexpress.feature.statistics.domain.usecase

import com.example.flashcardexpress.core.domain.model.Category
import com.example.flashcardexpress.core.domain.model.Question
import com.example.flashcardexpress.core.domain.repository.CategoryRepository
import com.example.flashcardexpress.core.domain.repository.QuestionRepository
import com.example.flashcardexpress.feature.statistics.domain.model.CategoryStatistics
import com.example.flashcardexpress.feature.statistics.domain.model.GlobalStatistics
import com.example.flashcardexpress.feature.statistics.domain.model.StageStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to retrieve aggregated global statistics for all flashcards and categories.
 *
 * Invoked by:
 * - [com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewViewModel]
 */
class GetGlobalStatisticsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val questionRepository: QuestionRepository
) {

    operator fun invoke(): Flow<GlobalStatistics> {
        return combine(
            categoryRepository.getAllCategories(),
            questionRepository.getAllQuestions()
        ) { categories, questions ->
            buildGlobalStatistics(categories, questions)
        }
    }

    private fun buildGlobalStatistics(
        categories: List<Category>,
        questions: List<Question>
    ): GlobalStatistics {
        val today = LocalDate.now()
        val totalLearned = categories.sumOf { it.learnedWordsCount }
        val activeCount = questions.size
        val totalCount = totalLearned + activeCount
        val masteryPercentage = if (totalCount == 0) 0 else (totalLearned * 100) / totalCount
        val dueTodayCount = questions.count { !it.dateOfNextRepetition.isAfter(today) }

        val globalStages = buildStageStatistics(questions, totalLearned)
        val categoryStatsList = categories.map { category ->
            val categoryQuestions = questions.filter { it.categoryId == category.id }
            buildCategoryStatistics(category, categoryQuestions, today)
        }

        return GlobalStatistics(
            totalWordsCount = totalCount,
            learnedWordsCount = totalLearned,
            activeWordsCount = activeCount,
            masteryPercentage = masteryPercentage,
            wordsReadyForTodayCount = dueTodayCount,
            stageStatistics = globalStages,
            categoriesStatistics = categoryStatsList
        )
    }

    private fun buildCategoryStatistics(
        category: Category,
        questions: List<Question>,
        today: LocalDate
    ): CategoryStatistics {
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
