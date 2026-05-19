package com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.ConfirmationDialog
import com.example.flashcardexpress.common.ui.components.buttons.BackButton
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.common.ui.components.listWithTitle.EmptyListWithTitle
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.ui.model.ListTitle
import com.example.flashcardexpress.feature.questionManagement.presentation.components.ListWithTitleAndSpecialFirstElement
import com.example.flashcardexpress.feature.questionManagement.presentation.components.PlusButtonOverList

import kotlinx.coroutines.flow.Flow

/**
 * Screen showing the details of a specific flashcard category, including its name and associated questions.
 *
 * Available Functionalities:
 * - Deleting the current category (with confirmation).
 * - Navigating to the category editing screen.
 * - Viewing a list of all questions in this category.
 * - Deleting individual questions (with confirmation).
 * - Navigating to the question editing screen.
 * - Navigating to the question creation screen for this category.
 * - Returning to the Manage Panel.
 *
 * Key UI elements:
 * - [ManageButtons]: Buttons for editing or deleting the category.
 * - [ListWithTitleAndSpecialFirstElement]: Displays the list of questions with an add button.
 * - [ConfirmationDialog]: Shown when deleting a category or question.
 * - [FlashcardSnackbar]: Displays feedback messages.
 *
 * Navigation events exposed:
 * - [CategoryDetailsEvent.OnEditCategoryClicked]: Triggered when the category edit button is clicked.
 * - [CategoryDetailsEvent.OnCreateQuestionClicked]: Triggered when the add question button is clicked.
 * - [CategoryDetailsEvent.OnQuestionEditClicked]: Triggered when a question's edit button is clicked.
 * - [CategoryDetailsEvent.OnBackToManagePanelClicked]: Triggered when the back button is clicked.
 *
 * Navigable from: [ManagePanelScreen].
 */
@Composable
fun CategoryDetailsScreen(state: CategoryDetailsState,
                          onEventFromViewModel: (CategoryDetailsEvent) -> Unit,
                          effectFromViewModel: Flow<CategoryDetailsEffect>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleEffectsFromViewModel(effectFromViewModel, snackbarHostState)
    if(state.isConfirmCategoryDeleteAlertVisible) {
        ConfirmationDialog(
            onConfirm = { onEventFromViewModel(CategoryDetailsEvent.OnConfirmDeleteCategoryClicked) },
            onDismiss = { onEventFromViewModel(CategoryDetailsEvent.OnAlertDismissClicked) },
            text = stringResource(R.string.delete_category_confirmation),


        )
    }
    if(state.isConfirmQuestionDeleteAlertVisible) {
        ConfirmationDialog(
            onConfirm = { onEventFromViewModel(CategoryDetailsEvent.OnConfirmDeleteQuestionClicked)},
            onDismiss = { onEventFromViewModel(CategoryDetailsEvent.OnAlertDismissClicked)},
            text = stringResource(R.string.delete_question_confirmation))
    }

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
                state.categoryName,
                style= MaterialTheme.typography.titleLarge

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Language: ${state.language}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimensions.marginTop))
            ManageButtons(onEventFromViewModel)




            if (state.questions.isEmpty()) {
                ShowEmptyList(onEventFromViewModel,modifier = Modifier.weight(1f))
            }else
            {
                Spacer(modifier = Modifier.height(30.dp))
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
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
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
    val listTitle=ListTitle(stringResource(R.string.repeat_panel_list_title), MaterialTheme.typography.titleMedium)
    val normalRowContent=@Composable {element: ElementForListWithTitle->
        QuestionRightPartOfRow(onEventFromViewModel, element)
    }
    ListWithTitleAndSpecialFirstElement(listTitle, listOfQuestions, modifier,normalRowContent) {
        PlusButtonOverList({
            onEventFromViewModel(CategoryDetailsEvent.OnCreateQuestionClicked)
        },R.string.btn_add_question)
    }
}


@Composable
private fun QuestionRightPartOfRow(onEventFromViewModel: (CategoryDetailsEvent) -> Unit,element: ElementForListWithTitle){

    Spacer(modifier = Modifier.width(AppDimensions.marginBetweenTextAndButtonsInRowOfList))
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
    Spacer(modifier = Modifier.width(5.dp))
    Button(
        onClick = { onEventFromViewModel(CategoryDetailsEvent.OnQuestionEditClicked(element.id)) },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            stringResource(R.string.list_of_questions_empty),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onEventFromViewModel(CategoryDetailsEvent.OnCreateQuestionClicked)

            },



        ) {
            Text(stringResource(R.string.btn_go_to_create_question))
        }
    }
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

