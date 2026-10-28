package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.ScreenComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R

/**
 * Text field component for searching and filtering questions in the list.
 *
 * Purpose:
 * Allows user to type a search query to filter flashcards by word or translation.
 *
 * Usage (Inputs/Outputs/State):
 * - [query]: Current text inside the search field.
 * - [onQueryChange]: Callback invoked when search input changes.
 * - [modifier]: Modifier for layout styling.
 *
 * Key UI elements:
 * - [OutlinedTextField]: Search input with search icon and clear button.
 *
 * Used In:
 * - [com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsScreen]
 */
@Composable
fun SearchQuestionsField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search_words_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.testTag("clearSearchButton")
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .testTag("searchQuestionsInput")
    )
}
