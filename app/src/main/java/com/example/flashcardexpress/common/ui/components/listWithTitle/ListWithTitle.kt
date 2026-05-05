package com.example.flashcardexpress.common.ui.components.listWithTitle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle

/**
 * A generic list component that displays a title and a collection of items.
 *
 * Purpose:
 * Provides a standardized way to display a scrollable list of items with a header title.
 *
 * Usage (Inputs/Outputs/State):
 * - [title]: Configuration for the list header (text and style).
 * - [listOfElements]: The data to be displayed in the list.
 * - [modifier]: Modifier for the outer container.
 * - [rowsContent]: Composable function defining the right part of each list row (e.g., action buttons).
 *
 * Key UI elements:
 * - [Text]: Displays the list title.
 * - [LazyColumn]: Efficiently renders only the visible items in the list.
 * - [CardForListWithTitle]: A styled container for each individual list item.
 *
 * Used In:
 * - [ListWithTitleAndSpecialFirstElement]
 * - Various screens for displaying lists of data.
 */
@Composable
fun<SpecificElementForListWithTitle: ElementForListWithTitle> ListWithTitle(title: ListTitle, listOfElements:List<SpecificElementForListWithTitle>, modifier: Modifier=Modifier, rowsContent: @Composable (SpecificElementForListWithTitle) -> Unit)
{

    val scrollState = rememberScrollState()
    Column(
        modifier= modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(AppDimensions.marginTop)
        )
        Text(
            title.title,
            style = title.fontStyle
        )
        Spacer(modifier= Modifier.height(10.dp))
        ShowElementsOfList(modifier, listOfElements, rowsContent)

    }




}

@Composable
fun <SpecificElementForListWithTitle : ElementForListWithTitle> ShowElementsOfList(
    modifier: Modifier,
    listOfElements: List<SpecificElementForListWithTitle>,
    rowsContent: @Composable ((SpecificElementForListWithTitle) -> Unit)
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,


        ) {
        items(items = listOfElements)
        { element ->
            CardForListWithTitle {
                RowForListWithTitle(element.elementName) {
                    rowsContent(element)
                }
            }
        }

    }
}


@Composable
public fun CardForListWithTitle(colors: CardColors=CardDefaults.elevatedCardColors(
    containerColor = MaterialTheme.colorScheme.surfaceVariant,
    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
),content: @Composable () -> Unit)
{
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimensions.marginBetweenRowsInList),
        colors = colors
    ) {
        content()
    }

}




