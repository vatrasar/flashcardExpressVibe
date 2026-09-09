package com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.ScreenComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.feature.statistics.domain.model.StageStatistics

/**
 * Card displaying words distribution across all repetition intervals for a category.
 *
 * Purpose:
 * Visualizes the count of flashcards in each learning stage within the selected category.
 *
 * Usage (Inputs/Outputs/State):
 * - [stages]: The [StageStatistics] data object holding counts for stages 0-4 and mastered.
 * - [modifier]: Modifier for container styling.
 *
 * Key UI elements:
 * - [Card]: Container card.
 * - [StageItemCard]: Individual stage item box displaying interval label and word count.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsScreen]
 */
@Composable
fun CategoryStageBreakdownCard(
    stages: StageStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("categoryStageBreakdownCard"),
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
            Text(
                text = stringResource(R.string.statistics_learning_stages_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            StagesGrid(stages = stages)
        }
    }
}

@Composable
private fun StagesGrid(stages: StageStatistics) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StageItemCard(
                label = stringResource(R.string.statistics_stage_today),
                count = stages.stage0TodayOrNewCount,
                modifier = Modifier.weight(1f)
            )
            StageItemCard(
                label = stringResource(R.string.statistics_stage_1_day),
                count = stages.stage1DayCount,
                modifier = Modifier.weight(1f)
            )
            StageItemCard(
                label = stringResource(R.string.statistics_stage_3_days),
                count = stages.stage3DaysCount,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StageItemCard(
                label = stringResource(R.string.statistics_stage_10_days),
                count = stages.stage10DaysCount,
                modifier = Modifier.weight(1f)
            )
            StageItemCard(
                label = stringResource(R.string.statistics_stage_30_days),
                count = stages.stage30DaysCount,
                modifier = Modifier.weight(1f)
            )
            StageItemCard(
                label = stringResource(R.string.statistics_stage_mastered),
                count = stages.masteredCount,
                isHighlight = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StageItemCard(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    val bgColor = if (isHighlight) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val textColor = if (isHighlight) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor.copy(alpha = 0.8f)
        )
    }
}
