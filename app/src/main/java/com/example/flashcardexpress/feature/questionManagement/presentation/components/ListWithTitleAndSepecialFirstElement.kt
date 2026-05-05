package com.example.flashcardexpress.feature.questionManagement.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.flashcardexpress.common.ui.components.listWithTitle.CardForListWithTitle
import com.example.flashcardexpress.common.ui.components.listWithTitle.RowForListWithTitle
import com.example.flashcardexpress.common.ui.components.listWithTitle.ShowElementsOfList
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle

/**
 * A generic list component that displays a title, a special first element, and a collection of items.
 *
 * Purpose:
 * Provides a standardized layout for lists that require a header action (like an "Add" button) followed by scrollable content.
 *
 * Usage (Inputs/Outputs/State):
 * - [title]: Configuration for the list header (text and style).
 * - [listOfElements]: The data to be displayed in the list.
 * - [modifier]: Modifier for the outer container.
 * - [rowsContent]: Composable function that defines how each item in [listOfElements] should be rendered.
 * - [firstRowContent]: Composable function for the special element at the top of the list (e.g., [PlusButtonOverList]).
 *
 * Key UI elements:
 * - [Text]: Displays the list title.
 * - [ShowElementsOfList]: Internal component that handles the rendering and scrolling of list items.
 *
 * Used In:
 * - [ManagePanelScreen]
 * - [CategoryDetailsScreen]
 * - [RepeatPanelScreen]
 */
@Composable
fun<SpecificElementForListWithTitle: ElementForListWithTitle> ListWithTitleAndSpecialFirstElement(
    title: ListTitle,
    listOfElements:List<SpecificElementForListWithTitle>,
    modifier: Modifier=Modifier,
    rowsContent: @Composable (SpecificElementForListWithTitle) -> Unit,
    firstRowContent: @Composable () -> Unit
)
{


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


        firstRowContent()
        ShowElementsOfList(modifier, listOfElements, rowsContent)





    }




}