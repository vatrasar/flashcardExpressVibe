package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R

/**
 * Header component for the link questions screen.
 *
 * Purpose:
 * Displays the screen navigation back button, centered title, and highlighted current source word context.
 *
 * Usage (Inputs/Outputs/State):
 * - [sourceWord]: The word being linked with other questions.
 * - [onBackClicked]: Callback when navigating back.
 * - [modifier]: Modifier for layout customization.
 *
 * Key UI elements:
 * - [IconButton]: Back button navigating back to repetition.
 * - [Text]: Centered screen title and styled subtitle highlighting the source word.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsScreen]
 */
@Composable
fun LinkQuestionsHeader(
    sourceWord: String,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefixText = stringResource(R.string.link_with_prefix)
    val annotatedSubtitle = buildAnnotatedString {
        append(prefixText)
        append(" ")
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("“$sourceWord”")
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier.testTag("backToRepetitionButton")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = stringResource(R.string.link_questions_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .testTag("linkQuestionsTitle")
            )
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = annotatedSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("linkQuestionsSubtitle")
        )
    }
}
