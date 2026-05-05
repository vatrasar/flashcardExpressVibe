package com.example.flashcardexpress.common.ui.components.listWithTitle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.model.ListTitle

/**
 * A layout component for displaying a list title and placeholder content for empty states.
 *
 * Purpose:
 * Provides a consistent structure for screens where a list is currently empty, allowing for informational text or call-to-action buttons.
 *
 * Usage (Inputs/Outputs/State):
 * - [title]: Configuration for the list header (text and style).
 * - [modifier]: Modifier for the outer container.
 * - [content]: Composable function defining the placeholder content (e.g., "List is empty" message or "Add" button).
 *
 * Key UI elements:
 * - [Text]: Displays the list title.
 * - [Box]: Centers the [content] vertically and horizontally within the available space.
 *
 * Used In:
 * - [ManagePanelScreen]
 * - [CategoryDetailsScreen]
 * - [RepeatPanelScreen]
 */
@Composable
fun EmptyListWithTitle(title: ListTitle, modifier: Modifier=Modifier, content: @Composable () -> Unit)
{

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier= modifier.fillMaxWidth(),

    ) {
        Spacer(
            modifier = Modifier
                .height(AppDimensions.marginTop)
        )
        Text(
            title.title,
            style = title.fontStyle)
        Box(
            modifier= Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center

        ) {
            content()
        }

    }
}

