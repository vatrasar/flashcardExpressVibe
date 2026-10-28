package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents.LinkQuestionsHeader
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents.QuestionLinkRow
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents.SearchQuestionsField
import kotlinx.coroutines.flow.Flow

/**
 * Screen for linking similar flashcard questions within a category.
 *
 * Available Functionalities:
 * - Filtering questions using a search text field.
 * - Selecting or deselecting questions to link with the active word.
 * - Navigating back to the active repetition session.
 *
 * Key UI elements:
 * - [LinkQuestionsHeader]: Top bar displaying title, subtitle, and back button.
 * - [SearchQuestionsField]: Search box for live filtering questions.
 * - [QuestionLinkRow]: List row representing each question with a checkbox.
 *
 * Navigation events exposed:
 * - [LinkQuestionsEvent.OnBackClicked]: Triggered when the back button is clicked.
 *
 * Navigable from: [com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionScreen].
 * Navigable to: [com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionScreen].
 */
@Composable
fun LinkQuestionsScreen(
    state: LinkQuestionsState,
    onEventFromViewModel: (LinkQuestionsEvent) -> Unit,
    effectFromViewModel: Flow<LinkQuestionsEffect>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                FlashcardSnackbar(snackbarData = data)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = AppDimensions.marginHorizontal)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LinkQuestionsHeader(
                sourceWord = state.sourceWord,
                onBackClicked = { onEventFromViewModel(LinkQuestionsEvent.OnBackClicked) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchQuestionsField(
                query = state.searchQuery,
                onQueryChange = { onEventFromViewModel(LinkQuestionsEvent.OnSearchQueryChanged(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            QuestionsListSection(
                state = state,
                onEventFromViewModel = onEventFromViewModel,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(AppDimensions.marginBottom))
        }
    }
}

@Composable
private fun QuestionsListSection(
    state: LinkQuestionsState,
    onEventFromViewModel: (LinkQuestionsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        LoadingIndicatorBox(modifier = modifier)
    } else if (state.filteredQuestions.isEmpty()) {
        EmptyQuestionsBox(modifier = modifier)
    } else {
        QuestionsLazyColumn(
            state = state,
            onEventFromViewModel = onEventFromViewModel,
            modifier = modifier
        )
    }
}

@Composable
private fun LoadingIndicatorBox(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(modifier = Modifier.testTag("linkQuestionsLoading"))
    }
}

@Composable
private fun EmptyQuestionsBox(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.no_questions_to_link),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("noQuestionsToLinkText")
        )
    }
}

@Composable
private fun QuestionsLazyColumn(
    state: LinkQuestionsState,
    onEventFromViewModel: (LinkQuestionsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("questionsList")
    ) {
        items(
            items = state.filteredQuestions,
            key = { it.question.id }
        ) { item ->
            QuestionLinkRow(
                item = item,
                onToggle = {
                    onEventFromViewModel(
                        LinkQuestionsEvent.OnToggleLink(item.question.id, item.isLinked)
                    )
                }
            )
        }
    }
}
