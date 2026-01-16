package com.example.flashcardexpress.feature.repeat.presentation.repeatPanel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.SnackbarType
import com.example.flashcardexpress.common.ui.components.listWithTitle.EmptyListWithTitle
import com.example.flashcardexpress.common.ui.components.listWithTitle.ListWithTitle
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsEvent
import com.example.flashcardexpress.feature.questionManagement.presentation.components.ListWithTitleAndSpecialFirstElement
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelEvent
import com.example.flashcardexpress.feature.repeat.domain.model.ElementForListWithCount

import kotlinx.coroutines.flow.Flow

@Composable
fun RepeatPanelScreen(
    onEventFromViewModel: (RepeatPanelEvent) -> Unit,
    effectFromViewModel: Flow<RepeatPanelEffect>,
    state: RepeatPanelState
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleEffectsFromViewModel(effectFromViewModel, snackbarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
            { data ->
                FlashcardSnackbar(snackbarData = data)

            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = AppDimensions.marginHorizontal)) {
            Spacer(modifier = Modifier.height(AppDimensions.marginTop))

            if(state.listOfRepetitions.isEmpty())
            {
                   ShowEmptyList(
                       onEventFromViewModel,
                       modifier = Modifier.weight(1f)
                   )
            }
            else{
                val listTitle=ListTitle(stringResource(R.string.repeat_panel_list_title), MaterialTheme.typography.titleLarge)
                val normalRowContent=@Composable { element: ElementForListWithTitle ->
                    RightPartOfRow(onEventFromViewModel, element as ElementForListWithCount)
                }
                ListWithTitleAndSpecialFirstElement(listTitle,state.listOfRepetitions,modifier = Modifier.weight(1f),normalRowContent) {
                    Spacer(modifier = Modifier.height(AppDimensions.marginForSpecialElementInList))
                    Row()  {
                        Text("Number of questions: ")
                        Text(text=state.totalNumberOfQuestions.toString(),
                            color= MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimensions.marginForSpecialElementInList))
                }

            }

        }
    }
}

@Composable
private fun RightPartOfRow(
    onEventFromViewModel: (RepeatPanelEvent) -> Unit,
    element: ElementForListWithCount
) {
    Spacer(modifier = Modifier.width(AppDimensions.marginBetweenTextAndButtonsInRowOfList))
    Button(
        onClick = { onEventFromViewModel(RepeatPanelEvent.OnRepetitionClicked(element.categoryId,element.count)) },
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
private fun ShowEmptyList(onEventFromViewModel: (RepeatPanelEvent) -> Unit, modifier: Modifier) {
    val listTitle=ListTitle(stringResource(R.string.list_of_questions), MaterialTheme.typography.titleMedium)
    EmptyListWithTitle(listTitle,modifier) {
        Text(
            stringResource(R.string.list_of_repetitions_is_empty),
            fontStyle = FontStyle.Italic
        )


    }
}






@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<RepeatPanelEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is RepeatPanelEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message, SnackbarType.SUCCESS.label, duration = SnackbarDuration.Short)


                }
            }

        }

    }
}

