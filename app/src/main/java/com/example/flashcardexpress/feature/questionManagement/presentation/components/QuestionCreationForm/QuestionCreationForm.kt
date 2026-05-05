package com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm

import android.graphics.drawable.Icon
import androidx.appcompat.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R.string
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm.CategoryFormActions


/**
 * Reusable form for entering flashcard details (question and answer).
 *
 * Purpose:
 * Provides a consistent UI for both creating new questions and editing existing ones.
 *
 * Usage (Inputs/Outputs/State):
 * - [inputValues]: Current values for the word and its translation.
 * - [pageTitle]: Title to be displayed at the top of the form.
 * - [actions]: Set of callbacks for text changes, saving, and navigating back.
 *
 * Key UI elements:
 * - [OutlinedTextField] for "Word" (question) input.
 * - [OutlinedTextField] for "Translation" (answer) input.
 * - "Save" button to trigger the saving logic.
 * - "Back" button to return to the previous screen.
 *
 * Used In:
 * - [CreationQuestionScreen]
 * - [QuestionEditScreen]
 */
@Composable
fun QuestionCreationForm(
    inputValues: QuestionCreationFormInputValues,
    pageTitle: String,
    actions: QuestionCreationFormActions
)
{
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = AppDimensions.marginHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        FormContent(inputValues, pageTitle, actions)

    }


}


@Composable
private fun FormContent(
    inputValues: QuestionCreationFormInputValues,
    pageTitle: String,
    actions: QuestionCreationFormActions
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TitleAndFormInputs(inputValues,pageTitle,actions)
    }

    Column() {
        Buttons(actions)
    }

}


@Composable
private fun TitleAndFormInputs(
    inputValues: QuestionCreationFormInputValues,
    pageTitle: String,
    actions: QuestionCreationFormActions


    ) {

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = pageTitle,
        style = MaterialTheme.typography.titleLarge,


        )
    Spacer(modifier = Modifier.height(AppDimensions.marginBetweenInputs))
    OutlinedTextField(
        onValueChange = {actions.onWordChanged(it) },
        value = inputValues.word,
        label = { Text(stringResource(string.word)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(AppDimensions.marginBetweenInputs))
    OutlinedTextField(
        onValueChange = {actions.onTranslationChanged(it) },
        value = inputValues.translation,
        label = { Text(stringResource(string.translation)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )



}


@Composable
private fun CategorySelect(
    actions: QuestionCreationFormActions,
    selectedCategory: CategorySelectOption?,
    categories: List<CategorySelectOption>

) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedButton(
        onClick = { expanded = true }


    ) {

        Text(text = selectedCategory?.name ?: stringResource(string.choose_category))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null
        )



    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }

    ) {
        if(categories.isEmpty())
        {
            DropdownMenuItem(
                text = { Text(stringResource(string.empty_category_list)) },
                onClick = { expanded = false }

            )
        }
        else{
            for (category in categories) {
                DropdownMenuItem(
                    text = { Text(category.name)},
                    onClick = {
                        actions.onCategoryChanged(category)
                        expanded = false

                    }

                )
            }
        }
    }
}



@Composable
private fun Buttons(
    actions: QuestionCreationFormActions
) {
    Button(
        onClick = { actions.onQuestionSave() },
        Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.save_question))
    }
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        onClick = { actions.onBack() }
        , colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.btn_back))
    }
    Spacer(modifier = Modifier.height(AppDimensions.marginBottom))
}