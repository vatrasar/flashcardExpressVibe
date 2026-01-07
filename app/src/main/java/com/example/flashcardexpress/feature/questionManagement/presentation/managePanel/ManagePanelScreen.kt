package com.example.flashcardexpress.feature.questionManagement.presentation.managePanel


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.buttons.BackButton
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.common.ui.components.listWithTitle.EmptyListWithTitle
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle
import com.example.flashcardexpress.feature.questionManagement.presentation.components.ListWithTitleAndSpecialFirstElement
import com.example.flashcardexpress.feature.questionManagement.presentation.components.PlusButtonOverList

import kotlinx.coroutines.channels.Channel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun ManagePanelScreen(state: ManagePanelState, onEventFromViewModel: (ManagePanelEvent) -> Unit, effectFromViewModel: Flow<ManagePanelEffect>) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleEffectsFromViewModel(effectFromViewModel,snackbarHostState)
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)
        {data->
            FlashcardSnackbar(snackbarData = data)

        }},
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = AppDimensions.marginHorizontal)) {

            if(state.listOfCategories.isEmpty())
            {
                ShowEmptyList(modifier = Modifier.weight(1f),onEventFromViewModel)
            }
            else{
                ShowListOfCategories(state.listOfCategories,onEventFromViewModel,modifier = Modifier.weight(1f))

            }
            BackButton {
                onEventFromViewModel(ManagePanelEvent.OnBackToHomeClicked)
            }
            Spacer(modifier = Modifier.height(AppDimensions.marginTop))


        }
    }
}

@Composable
fun ShowEmptyList(modifier: Modifier,onEventFromViewModel: (ManagePanelEvent) -> Unit) {
    val listTitle=ListTitle(stringResource(R.string.list_of_categories), MaterialTheme.typography.titleLarge)
    EmptyListWithTitle(listTitle,modifier) {
        Column() {
            TextAndButtonForEmptyList(onEventFromViewModel)

        }

    }
}

@Composable
private fun TextAndButtonForEmptyList(onEventFromViewModel: (ManagePanelEvent) -> Unit) {
    Text(
        stringResource(R.string.list_of_categories_empty),
        fontStyle = FontStyle.Italic
    )

    Spacer(modifier = Modifier.height(10.dp))
    Button(
        onClick = {
            onEventFromViewModel(ManagePanelEvent.OnNavigateToCategoryCreation)

        }
    ) {
        Text(stringResource(R.string.btn_go_to_create_first_category))
    }
}

@Composable
fun ShowListOfCategories(listOfCategories: List<ElementForListWithTitle>, onEventFromViewModel: (ManagePanelEvent) -> Unit,modifier: Modifier) {


    val listTitle=ListTitle(stringResource(R.string.list_of_categories), MaterialTheme.typography.titleLarge)
    val normalRowContent=@Composable {element: ElementForListWithTitle->
        CategoryRightPartOfRow(onEventFromViewModel, element)
    }
    ListWithTitleAndSpecialFirstElement(listTitle, listOfCategories,modifier,normalRowContent) {
        PlusButtonOverList({
            onEventFromViewModel(ManagePanelEvent.OnNavigateToCategoryCreation)
        },R.string.btn_add_category)
    }
}



@Composable
private fun CategoryRightPartOfRow(
    onEventFromViewModel: (ManagePanelEvent) -> Unit,
    title: ElementForListWithTitle
) {
    Spacer(modifier = Modifier.width(AppDimensions.marginBetweenTextAndButtonsInRowOfList))
    Button(
        onClick = { onEventFromViewModel(ManagePanelEvent.OnCategoryClicked(title)) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }

}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<ManagePanelEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->


        }

    }
}


@Preview
@Composable
fun ManagePanelScreenPreview() {
    ManagePanelScreen(ManagePanelState(listOf()),onEventFromViewModel = {},Channel<ManagePanelEffect>().receiveAsFlow())
}