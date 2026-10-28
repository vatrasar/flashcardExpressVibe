package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionWithLinkStatus

/**
 * List row component displaying a question and a checkbox for linking with dynamic selection styling.
 *
 * Purpose:
 * Renders an individual flashcard item with toggleable linked status and animated color transitions.
 *
 * Usage (Inputs/Outputs/State):
 * - [item]: The question data and its linked state.
 * - [onToggle]: Callback triggered when checking/unchecking the item.
 * - [modifier]: Modifier for container styling.
 *
 * Key UI elements:
 * - [Card]: Interactive container with animated background and border upon selection.
 * - [Checkbox]: Toggle reflecting linked state.
 * - [Text]: Word label.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsScreen]
 */
@Composable
fun QuestionLinkRow(
    item: QuestionWithLinkStatus,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (item.isLinked) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "cardContainerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (item.isLinked) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "cardContentColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (item.isLinked) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        label = "cardBorderColor"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.5.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .testTag("questionLinkRow_${item.question.id}")
            .clickable(onClick = onToggle)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.question.word,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = item.isLinked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("linkCheckbox_${item.question.id}")
            )
        }
    }
}
