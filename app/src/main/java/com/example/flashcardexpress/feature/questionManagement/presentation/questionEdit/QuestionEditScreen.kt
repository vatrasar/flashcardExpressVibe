package com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationForm
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationFormActions
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationFormInputValues
import com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion.CreationQuestionEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun QuestionEditScreen(
    onEventFromViewModel: (QuestionEditEvent) -> Unit,
    effectFromViewModel: Flow<QuestionEditEffect>,
    state: QuestionEditState
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
                onBack = { onEventFromViewModel(QuestionEditEvent.OnBackToCategoryDetailsClicked) },
                onWordChanged = { onEventFromViewModel(QuestionEditEvent.OnWordChanged(it)) },
                onTranslationChanged = { onEventFromViewModel(QuestionEditEvent.OnTranslationChanged(it)) },
                onCategoryChanged = {  },
                onQuestionSave = { onEventFromViewModel(QuestionEditEvent.OnSaveQuestionClicked) }
            )
            val questionCreationValues= QuestionCreationFormInputValues(
                state.word,
                state.translation
            )
            QuestionCreationForm(questionCreationValues,
                stringResource( R.string.question_edit_page_title),actions)

        }
    }
}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<QuestionEditEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is QuestionEditEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.resultType
                    )
                }
            }
        }

    }
}

