package com.example.flashcardexpress.feature.repeat.presentation.repetition

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.SegmentedProgressBar
import com.example.flashcardexpress.common.ui.components.buttons.BaseButton
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepetitionScreen(
    onEventFromViewModel: (RepetitionEvent) -> Unit,
    effectFromViewModel: Flow<RepetitionEffect>,
    state: RepetitionState
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

        Column (modifier = Modifier.padding(paddingValues)
            .padding(horizontal = AppDimensions.marginHorizontal)
            ) {


            TopRow(onEventFromViewModel,state.isAnswerPage)
            Spacer(modifier = Modifier.height(20.dp))
            SegmentedProgressBar(6,state.learningStage)
            Spacer(modifier = Modifier.height(70.dp))
            FlashcardColumn(state, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(70.dp))
            if(state.isAnswerPage)
            {
                ButtonsSectionAnswerPage(onEventFromViewModel)
            }
            else{
                ButtonsSectionQuestionPage(onEventFromViewModel)
            }

            Spacer(modifier = Modifier.height(AppDimensions.marginBottom))




        }
    }
}

@Composable
private fun ButtonsSectionAnswerPage(onEventFromViewModel: (RepetitionEvent) -> Unit, modifier: Modifier=Modifier) {
    Column(modifier=modifier) {

        BaseButton(
            stringResource(R.string.btn_know),
            { onEventFromViewModel(RepetitionEvent.OnAnswerCorrect) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary

            )
        )
        BaseButton(
            stringResource(R.string.btn_don_know),
            { onEventFromViewModel(RepetitionEvent.OnAnswerIncorrect) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary

            )
        )
    }
}

@Composable
private fun ButtonsSectionQuestionPage(onEventFromViewModel: (RepetitionEvent) -> Unit, modifier: Modifier=Modifier) {
    Column(modifier=modifier) {

        BaseButton(
            stringResource(R.string.btn_check),
            { onEventFromViewModel(RepetitionEvent.OnShowAnswer) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary

            )
        )

    }
}


@Composable
private fun FlashcardColumn(state: RepetitionState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().
        clip(RoundedCornerShape(16.dp)).
            background(color = MaterialTheme.colorScheme.surfaceVariant)
    , verticalArrangement = Arrangement.Center,
        ) {
        var titleText=stringResource(R.string.question)
        var flashcardText=state.flashcard.question
        if (state.isAnswerPage)
        {
            titleText=stringResource(R.string.answer)
            flashcardText=state.flashcard.answer
        }
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = flashcardText,
                textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant)

    }
}

@Composable
private fun TopRow(
    onEventFromViewModel: (RepetitionEvent) -> Unit,isAnswerPage: Boolean = true,
    modifier: Modifier = Modifier

) {

    var icon= Icons.Default.Close

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        if(!isAnswerPage)
        {
            CancelRepetitionBtn(onEventFromViewModel,RepetitionEvent.OnCancelRepetition, icon)
        }
        else{
            BackToQuestionsBtn(onEventFromViewModel)
        }

        Text("Repetition", textAlign = TextAlign.Center)
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
                
            )
        }


    }
}

@Composable
private fun BackToQuestionsBtn(onEventFromViewModel: (RepetitionEvent) -> Unit) {
    IconButton(onClick = {onEventFromViewModel(RepetitionEvent.OnBackToQuestion)}) {
        Icon(
            imageVector = Icons.Default.Refresh, contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface

        )
    }
}

@Composable
private fun CancelRepetitionBtn(
    onEventFromViewModel: (RepetitionEvent) -> Unit,
    action: RepetitionEvent.OnCancelRepetition,
    icon: ImageVector
) {
    IconButton(onClick = { onEventFromViewModel(action) }) {
        Icon(
            imageVector = icon, contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<RepetitionEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->

        }

    }
}

@Preview
@Composable
fun RepetitionScreenPreview() {
    RepetitionScreen(onEventFromViewModel = {event:RepetitionEvent->}, effectFromViewModel = flowOf(), state =
        RepetitionState(true, Flashcard("default question", "default translation")
        ,1))
}

