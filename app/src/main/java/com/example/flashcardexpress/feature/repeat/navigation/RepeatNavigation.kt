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
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelNavEffect
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelScreen
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelViewModel
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
        RepetitionScreen(viewModel::onEvent,viewModel.effect,state)


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
                    {

                    }
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