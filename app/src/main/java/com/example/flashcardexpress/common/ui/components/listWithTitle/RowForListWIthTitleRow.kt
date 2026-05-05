package com.example.flashcardexpress.common.ui.components.listWithTitle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A standardized row layout for list items.
 *
 * Purpose:
 * Provides a consistent structure for list rows, with an item label on the left and custom actions on the right.
 *
 * Usage (Inputs/Outputs/State):
 * - [rowText]: The main text to be displayed for the list item (e.g., category name, question).
 * - [content]: Composable function defining the right part of the row, usually containing action buttons.
 *
 * Key UI elements:
 * - [Row]: Horizontally aligns the text and the custom content with space between them.
 * - [Text]: Displays [rowText] with ellipsis if it's too long to fit in a single line.
 *
 * Used In:
 * - [ListWithTitle]
 * - [ShowElementsOfList]
 */
@Composable
fun RowForListWithTitle(rowText:String, content: @Composable () -> Unit)
{
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            rowText,
            modifier = Modifier.padding(horizontal = 8.dp)
                .weight(1f),
            overflow = TextOverflow.Ellipsis
            , maxLines = 1

        )


        content()

    }

}