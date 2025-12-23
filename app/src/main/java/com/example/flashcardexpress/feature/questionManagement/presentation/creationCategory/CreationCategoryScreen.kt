package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.example.flashcardexpress.R.*
import com.example.flashcardexpress.common.flashcardSnackbar.FlashcardSnackbar
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@Composable
fun CreationCategoryScreen(state: CreationCategoryState, effectFromViewModel: Flow<CreationCategoryEffect>, onEventFromViewModel:(CreationCategoryEvent)->Unit)
{
    val snackbarHostState = remember { SnackbarHostState() }

    handleEffectsFromViewModel(effectFromViewModel, snackbarHostState)
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)
        {data->
            FlashcardSnackbar(snackbarData = data)

        }},
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            CategoryCreationForm(state, onEventFromViewModel)
        }
    }
}

@Composable
private fun handleEffectsFromViewModel(
    effectFromViewModel: Flow<CreationCategoryEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is CreationCategoryEffect.ShowSnackbar -> {

                        snackbarHostState.showSnackbar(message = effect.message, actionLabel = effect.resultType,
                            duration = SnackbarDuration.Short)

                }
            }
        }

    }
}

@Composable
private fun CategoryCreationForm(state: CreationCategoryState,onEventFromViewModel:(CreationCategoryEvent)->Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        FormContent(onEventFromViewModel, state)

    }
}

@Composable
private fun FormContent(
    onEventFromViewModel: (CreationCategoryEvent) -> Unit,
    state: CreationCategoryState
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TitleAndFormInput(onEventFromViewModel, state)
    }

    Column() {
        Buttons(onEventFromViewModel)
    }

}

@Composable
private fun Buttons(onEventFromViewModel: (CreationCategoryEvent) -> Unit) {
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
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun TitleAndFormInput(
    onEventFromViewModel: (CreationCategoryEvent) -> Unit,
    state: CreationCategoryState
) {

    Spacer(modifier = Modifier.height(10.dp))
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
    CreationCategoryScreen(state,Channel<CreationCategoryEffect>().receiveAsFlow(),onEventFromViewModel = {})
}