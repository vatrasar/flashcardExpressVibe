package com.example.flashcardexpress.feature.statistics.presentation.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.feature.statistics.domain.model.CategoryStatistics
import com.example.flashcardexpress.feature.statistics.domain.model.GlobalStatistics
import com.example.flashcardexpress.feature.statistics.presentation.overview.ScreenComponents.CategoryStatCard
import com.example.flashcardexpress.feature.statistics.presentation.overview.ScreenComponents.MasteryProgressBar
import com.example.flashcardexpress.feature.statistics.presentation.overview.ScreenComponents.StageBreakdownCard
import kotlinx.coroutines.flow.Flow

/**
 * Main statistics overview screen.
 *
 * Purpose:
 * Displays global learning progress, distribution across spaced repetition stages,
 * and a list of all categories with individual summaries.
 *
 * Available Functionalities:
 * - View aggregate mastery percentage and active vs mastered flashcard counts.
 * - View breakdown of words waiting in each repetition interval (Today/New, 1d, 3d, 10d, 30d, Mastered).
 * - Browse all categories and navigate to a category's detailed statistics screen.
 *
 * Key UI elements:
 * - [MasteryProgressBar]: Global mastery percentage and total word counts.
 * - [StageBreakdownCard]: Grid of spaced repetition interval cards.
 * - [LazyColumn]: Scrollable container presenting the overview and category cards.
 * - [CategoryStatCard]: Card for each category leading to detailed statistics.
 *
 * Navigation events exposed:
 * - Navigates to [com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsScreen] via bottom bar or category click.
 * Can be navigated to from: [com.example.flashcardexpress.navigation.components.AppBottomBar].
 */
@Composable
fun StatisticsOverviewScreen(
    state: StatisticsOverviewState,
    onEvent: (StatisticsOverviewEvent) -> Unit,
    effect: Flow<StatisticsOverviewEffect>,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        effect.collect {
        }
    }

    if (state.isLoading) {
        LoadingIndicator(modifier = modifier)
    } else {
        StatisticsOverviewContent(
            globalStatistics = state.globalStatistics,
            onCategoryClick = { categoryId ->
                onEvent(StatisticsOverviewEvent.OnCategoryClicked(categoryId))
            },
            modifier = modifier
        )
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("statisticsLoadingIndicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun StatisticsOverviewContent(
    globalStatistics: GlobalStatistics?,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("statisticsOverviewList"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenTitle()
        }

        if (globalStatistics != null) {
            item {
                MasteryProgressBar(
                    masteryPercentage = globalStatistics.masteryPercentage,
                    totalWords = globalStatistics.totalWordsCount,
                    masteredWords = globalStatistics.learnedWordsCount,
                    activeWords = globalStatistics.activeWordsCount,
                    dueTodayWords = globalStatistics.wordsReadyForTodayCount
                )
            }

            item {
                StageBreakdownCard(stages = globalStatistics.stageStatistics)
            }

            item {
                CategoriesSectionHeader()
            }

            if (globalStatistics.categoriesStatistics.isEmpty()) {
                item {
                    EmptyCategoriesMessage()
                }
            } else {
                items(
                    items = globalStatistics.categoriesStatistics,
                    key = { it.categoryId }
                ) { category ->
                    CategoryStatCard(
                        category = category,
                        onClick = { onCategoryClick(category.categoryId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenTitle() {
    Text(
        text = stringResource(R.string.statistics_title),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun CategoriesSectionHeader() {
    Text(
        text = stringResource(R.string.statistics_categories_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun EmptyCategoriesMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.statistics_no_categories),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
