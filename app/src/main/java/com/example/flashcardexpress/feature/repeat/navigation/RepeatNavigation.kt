package com.example.flashcardexpress.feature.repeat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen.*
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsNavEffect
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsScreen
import com.example.flashcardexpress.feature.repeat.presentation.linkQuestions.LinkQuestionsViewModel
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelNavEffect
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelScreen
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelViewModel
import com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionEvent
import com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionNavEffect
import com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionScreen
import com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionViewModel

fun NavGraphBuilder.setupRepeatNavigation(navController: NavController) {

    composable<RepeatScreen.RepeatPanel> {
        val viewModel: RepeatPanelViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleNavEffectsFromRepeatScreen(viewModel, navController)
        RepeatPanelScreen(viewModel::onEvent,viewModel.effect,state)

    }
    composable<RepeatScreen.Repetition> {
        val viewModel: RepetitionViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleNavEffectsFromRepetition(viewModel, navController)
        ObserveReturnFromLinkQuestions(navController, viewModel)
        RepetitionScreen(viewModel::onEvent,viewModel.effect,state)
    }
    composable<RepeatScreen.LinkQuestions> {
        val viewModel: LinkQuestionsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleNavEffectsFromLinkQuestions(viewModel, navController)
        LinkQuestionsScreen(state, viewModel::onEvent, viewModel.effect)
    }
}

@Composable
private fun ObserveReturnFromLinkQuestions(
    navController: NavController,
    viewModel: RepetitionViewModel
) {
    val backStackEntry = navController.currentBackStackEntry
    val shouldSync = backStackEntry?.savedStateHandle?.getStateFlow("should_sync_linked_questions", false)
        ?.collectAsStateWithLifecycle()
    LaunchedEffect(shouldSync?.value) {
        if (shouldSync?.value == true) {
            viewModel.onEvent(RepetitionEvent.OnReturnFromLinkQuestions)
            backStackEntry.savedStateHandle["should_sync_linked_questions"] = false
        }
    }
}

@Composable
private fun HandleNavEffectsFromLinkQuestions(
    viewModel: LinkQuestionsViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { navEffect ->
            when (navEffect) {
                LinkQuestionsNavEffect.NavigateBack -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("should_sync_linked_questions", true)
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
private fun HandleNavEffectsFromRepetition(
    viewModel: RepetitionViewModel,
    navController: NavController
)
{
    LaunchedEffect(viewModel.navEffect){
        viewModel.navEffect.collect{navEffect->
            when(navEffect)
            {
                is RepetitionNavEffect.BackToRepeatPanel -> {
                    navController.popBackStack()
                }

                RepetitionNavEffect.BackToRepeatPanelAfterFinishingRepetition -> {
                    navController.navigate(RepeatScreen.RepeatPanel(true))
                }

                is RepetitionNavEffect.NavigateToLinkQuestions -> {
                    navController.navigate(
                        RepeatScreen.LinkQuestions(
                            questionId = navEffect.questionId,
                            categoryId = navEffect.categoryId,
                            sourceWord = navEffect.sourceWord
                        )
                    )
                }
            }
        }
    }
}
@Composable
private fun HandleNavEffectsFromRepeatScreen(
    viewModel: RepeatPanelViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { navEffect ->
            when (navEffect) {
                is RepeatPanelNavEffect.NavigateToRepetition -> {
                    navController.navigate(RepeatScreen.Repetition(navEffect.categoryId,navEffect.numberOfQuestionsInRepetition))
                }
            }
        }


    }
}