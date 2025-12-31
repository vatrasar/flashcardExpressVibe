package com.example.flashcardexpress.common.ui.components.listWithTitle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun<SpecificElementForListWithTitle: ElementForListWithTitle> ListWithTitle(title: ListTitle, listOfElements:List<SpecificElementForListWithTitle>, modifier: Modifier=Modifier, rowsContent: @Composable (SpecificElementForListWithTitle) -> Unit)
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
        for(element in listOfElements)
        {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().padding(vertical = AppDimensions.marginBetweenRowsInList),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                RowForListWithTitle(element.elementName) {
                    rowsContent(element)
                }
            }


        }

    }




}




