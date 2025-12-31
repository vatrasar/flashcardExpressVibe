package com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails

import androidx.annotation.Dimension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.flashcardexpress.common.ui.components.listWithTitle.ListWithTitle
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelEvent

import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ShowListOfCategories
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun CategoryDetailsScreen(state: CategoryDetailsState,
                          onEventFromViewModel: (CategoryDetailsEvent) -> Unit,
                          effectFromViewModel: Flow<CategoryDetailsEffect>
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

        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = AppDimensions.marginHorizontal)
            , horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(AppDimensions.marginTop))
            Text(
                state.categoryName+" "+stringResource(R.string.details_label),
                style= MaterialTheme.typography.titleLarge

            )

            Spacer(modifier = Modifier.height(AppDimensions.marginTop))
            ManageButtons(onEventFromViewModel)




            if (state.questions.isEmpty()) {
                ShowEmptyList(onEventFromViewModel,modifier = Modifier.weight(1f))
            }else
            {
                ShowListOfQuestions(state.questions,onEventFromViewModel,modifier = Modifier.weight(1f))
            }
            BackButton {
                onEventFromViewModel(CategoryDetailsEvent.OnBackToManagePanelClicked)
            }
            Spacer(modifier = Modifier.height(AppDimensions.marginTop))





        }
    }
}


@Composable
private fun ManageButtons(onEventFromViewModel: (CategoryDetailsEvent) -> Unit)
{

    Column  {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.fillMaxWidth()
            ,
            onClick = { onEventFromViewModel(CategoryDetailsEvent.OnDeleteCategoryClicked) },
        ) {
            Text(stringResource(R.string.delete))


        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEventFromViewModel(CategoryDetailsEvent.OnEditCategoryClicked) }
        ) {
            Text(stringResource(R.string.edit))


        }
    }

}

@Composable
private fun ShowListOfQuestions(listOfQuestions: List<ElementForListWithTitle>,onEventFromViewModel: (CategoryDetailsEvent) -> Unit,modifier: Modifier) {
    val listTitle=ListTitle(stringResource(R.string.list_of_questions), MaterialTheme.typography.titleMedium)
    ListWithTitle(listTitle, listOfQuestions, modifier) {
        QuestionRightPartOfRow(onEventFromViewModel, it)
    }
}


@Composable
private fun QuestionRightPartOfRow(onEventFromViewModel: (CategoryDetailsEvent) -> Unit,element: ElementForListWithTitle){

    Button(
        onClick = { onEventFromViewModel(CategoryDetailsEvent.OnQuestionRemoveClicked(element.id)) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
    Button(
        onClick = { onEventFromViewModel(CategoryDetailsEvent.OnQuestionEditClicked(element.id)) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null
        )
    }
}

@Composable
private fun ShowEmptyList(onEventFromViewModel: (CategoryDetailsEvent) -> Unit, modifier: Modifier) {
    val listTitle=ListTitle(stringResource(R.string.list_of_questions), MaterialTheme.typography.titleMedium)
    EmptyListWithTitle(listTitle,modifier) {
        TextAndButtonForEmptyList(onEventFromViewModel)


    }
}

@Composable
private fun TextAndButtonForEmptyList(onEventFromViewModel: (CategoryDetailsEvent) -> Unit) {
    Text(
        stringResource(R.string.list_of_questions_empty),
        fontStyle = FontStyle.Italic
    )

    Spacer(modifier = Modifier.height(10.dp))
//    Button(
//        onClick = {
//            onEventFromViewModel(ManagePanelEvent.OnNavigateToCategoryCreation)
//
//        }
//    ) {
//        Text(stringResource(R.string.btn_go_to_create_first_category))
//    }
}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<CategoryDetailsEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->

        }

    }
}

@Preview
@Composable
fun CategoryDetailsScreenPreview() {
    CategoryDetailsScreen(CategoryDetailsState(listOf(),"Test"),
        onEventFromViewModel = {},
        Channel<CategoryDetailsEffect>().receiveAsFlow()
    )
}