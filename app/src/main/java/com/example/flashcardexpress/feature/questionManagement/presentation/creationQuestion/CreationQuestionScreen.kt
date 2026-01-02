package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationForm
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationFormActions
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationFormInputValues
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun CreationQuestionScreen(
    state: CreationQuestionState,
    onEventFromViewModel: (CreationQuestionEvent) -> Unit,
    effectFromViewModel: Flow<CreationQuestionEffect>
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

        Box(modifier = Modifier.padding(paddingValues)) {
            val actions = QuestionCreationFormActions(
                onBack = { onEventFromViewModel(CreationQuestionEvent.OnBackToCreationMenuClicked) },
                onWordChanged = { onEventFromViewModel(CreationQuestionEvent.OnWordChanged(it)) },
                onTranslationChanged = { onEventFromViewModel(CreationQuestionEvent.OnTranslationChanged(it)) },
                onCategoryChanged = { onEventFromViewModel(CreationQuestionEvent.OnCategoryChanged(it)) },
                onQuestionSave = { onEventFromViewModel(CreationQuestionEvent.OnSaveQuestionClicked) }
            )
            val questionCreationValues= QuestionCreationFormInputValues(
                state.word,
                state.translation,
                state.categorySelected,
                state.categories
            )
            QuestionCreationForm(questionCreationValues,
                stringResource( R.string.question_creation_page_title),actions)

        }
    }
}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<CreationQuestionEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is CreationQuestionEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short,
                        actionLabel = effect.resultType
                    )
                }
            }


        }

    }
}

