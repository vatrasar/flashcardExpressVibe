package com.example.flashcardexpress.feature.statistics.presentation.overview.ScreenComponents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardexpress.R

/**
 * Modern card displaying mastery rate and progress bar.
 *
 * Purpose:
 * Visualizes the percentage of mastered flashcards out of total words with animated progress.
 *
 * Usage (Inputs/Outputs/State):
 * - [masteryPercentage]: The percentage of mastered words (0 to 100).
 * - [totalWords]: The total number of words (active + mastered).
 * - [masteredWords]: The count of mastered words.
 * - [activeWords]: The count of words currently in learning stages.
 * - [dueTodayWords]: The count of words waiting for review today.
 * - [modifier]: Modifier for container styling.
 *
 * Key UI elements:
 * - [Card]: Container surface with elevation and styling.
 * - [Text]: Informational labels and percentages.
 * - [Box]: Animated custom progress bar track and indicator.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewScreen]
 */
@Composable
fun MasteryProgressBar(
    masteryPercentage: Int,
    totalWords: Int,
    masteredWords: Int,
    activeWords: Int,
    dueTodayWords: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (masteryPercentage.coerceIn(0, 100)) / 100f,
        label = "masteryProgress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("overviewMasteryCard"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MasteryHeaderRow(masteryPercentage = masteryPercentage)
            ProgressBarIndicator(progress = animatedProgress)
            MasteryStatsRow(
                totalWords = totalWords,
                masteredWords = masteredWords,
                activeWords = activeWords,
                dueTodayWords = dueTodayWords
            )
        }
    }
}

@Composable
private fun MasteryHeaderRow(masteryPercentage: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.statistics_overall_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.statistics_percentage_format, masteryPercentage),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag("masteryPercentageText")
        )
    }
}

@Composable
private fun ProgressBarIndicator(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceAtLeast(0.01f))
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun MasteryStatsRow(
    totalWords: Int,
    masteredWords: Int,
    activeWords: Int,
    dueTodayWords: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItemColumn(
            label = stringResource(R.string.statistics_total_words),
            value = totalWords.toString()
        )
        StatItemColumn(
            label = stringResource(R.string.statistics_learned_words),
            value = masteredWords.toString()
        )
        StatItemColumn(
            label = stringResource(R.string.statistics_learning_words),
            value = activeWords.toString()
        )
        StatItemColumn(
            label = stringResource(R.string.statistics_due_today),
            value = dueTodayWords.toString()
        )
    }
}

@Composable
private fun StatItemColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
        )
    }
}
