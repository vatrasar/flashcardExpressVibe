package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import com.example.flashcardexpress.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.flashcardexpress.R.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow
import org.intellij.lang.annotations.JdkConstants

@Composable
fun CreationCategoryScreen(state: CreationCategoryState, onEventFromViewModel:(CreationCategoryEvent)->Unit)
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CategoryCreationForm(state,onEventFromViewModel)

    }
}

@Composable
private fun CategoryCreationForm(state: CreationCategoryState,onEventFromViewModel:(CreationCategoryEvent)->Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        formContent(onEventFromViewModel, state)

    }
}

@Composable
private fun formContent(
    onEventFromViewModel: (CreationCategoryEvent) -> Unit,
    state: CreationCategoryState
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        titleAndFormInput(onEventFromViewModel, state)
    }

    Column() {
        buttons(onEventFromViewModel)
    }

}

@Composable
private fun buttons(onEventFromViewModel: (CreationCategoryEvent) -> Unit) {
    Button(
        onClick = { onEventFromViewModel(CreationCategoryEvent.OnAddCategoryClicked) },
        Modifier
            .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.add_category_btn_text))
    }
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        onClick = { onEventFromViewModel(CreationCategoryEvent.OnBackToCreationMenuClicked) }
        , colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.btn_back))
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun titleAndFormInput(
    onEventFromViewModel: (CreationCategoryEvent) -> Unit,
    state: CreationCategoryState
) {

    Spacer(modifier = Modifier.height(40.dp))
    Text(
        text = stringResource(string.add_category_page_title),
        style = MaterialTheme.typography.titleLarge,


        )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        onValueChange = { onEventFromViewModel(CreationCategoryEvent.OnCategoryNameChanged(it)) },
        value = state.categoryName,
        label = { Text(stringResource(string.add_category_label)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun CreationCategoryScreenPreview() {
    val state= CreationCategoryState("")
    CreationCategoryScreen(state,onEventFromViewModel = {})
}