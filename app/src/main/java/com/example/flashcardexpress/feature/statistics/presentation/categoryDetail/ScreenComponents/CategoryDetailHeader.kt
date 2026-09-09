package com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.ScreenComponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight

/**
 * Top title for category statistics detail screen.
 *
 * Purpose:
 * Displays the name of the selected category.
 *
 * Usage (Inputs/Outputs/State):
 * - [categoryName]: The display name of the category.
 * - [modifier]: Modifier for container styling.
 *
 * Key UI elements:
 * - [Text]: Category title.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsScreen]
 */
@Composable
fun CategoryDetailHeader(
    categoryName: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = categoryName,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.testTag("categoryDetailTitle")
    )
}
