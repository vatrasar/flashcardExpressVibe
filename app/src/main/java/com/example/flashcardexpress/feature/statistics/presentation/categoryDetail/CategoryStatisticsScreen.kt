package com.example.flashcardexpress.feature.statistics.presentation.categoryDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.ui.components.buttons.BackButton
import com.example.flashcardexpress.feature.statistics.domain.model.CategoryStatistics
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.ScreenComponents.CategoryDetailHeader
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.ScreenComponents.CategoryProgressCard
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.ScreenComponents.CategoryStageBreakdownCard
import kotlinx.coroutines.flow.Flow

/**
 * Screen displaying detailed statistics for a specific category.
 *
 * Purpose:
 * Provides in-depth metrics and interval breakdown for all flashcards within the selected category.
 *
 * Available Functionalities:
 * - View category-specific mastery percentage, total words, active words, and mastered words.
 * - View distribution of cards across stages: Today/New, 1d, 3d, 10d, 30d, Mastered.
 * - Navigate back to the main statistics overview screen using the bottom back button.
 *
 * Key UI elements:
 * - [CategoryDetailHeader]: Header with category title.
 * - [CategoryProgressCard]: Mastery percentage and counts card.
 * - [CategoryStageBreakdownCard]: Grid of repetition intervals.
 * - [LazyColumn]: Scrollable container for the detailed statistics cards.
 * - [BackButton]: Placed at the bottom of the screen to navigate back.
 *
 * Navigation events exposed:
 * - Emits [CategoryStatisticsEvent.OnBackClicked] to navigate back to [com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewScreen].
 * Can be navigated to from: [com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewScreen].
 */
@Composable
fun CategoryStatisticsScreen(
    state: CategoryStatisticsState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    effect: Flow<CategoryStatisticsEffect>,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        effect.collect {
        }
    }

    if (state.isLoading) {
        CategoryLoadingIndicator(modifier = modifier)
    } else {
        CategoryStatisticsContent(
            categoryStatistics = state.categoryStatistics,
            onBackClick = { onEvent(CategoryStatisticsEvent.OnBackClicked) },
            modifier = modifier
        )
    }
}

@Composable
private fun CategoryLoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("categoryStatisticsLoadingIndicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CategoryStatisticsContent(
    categoryStatistics: CategoryStatistics?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (categoryStatistics != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("categoryStatisticsList"),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    CategoryDetailHeader(categoryName = categoryStatistics.categoryName)
                }

                item {
                    CategoryProgressCard(
                        masteryPercentage = categoryStatistics.masteryPercentage,
                        totalWords = categoryStatistics.totalWordsCount,
                        masteredWords = categoryStatistics.learnedWordsCount,
                        activeWords = categoryStatistics.activeWordsCount,
                        dueTodayWords = categoryStatistics.wordsReadyForTodayCount
                    )
                }

                item {
                    CategoryStageBreakdownCard(stages = categoryStatistics.stageStatistics)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                EmptyCategoryStatsMessage()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        BackButton(onEventFromViewModel = onBackClick)
    }
}

@Composable
private fun EmptyCategoryStatsMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.statistics_no_words),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
