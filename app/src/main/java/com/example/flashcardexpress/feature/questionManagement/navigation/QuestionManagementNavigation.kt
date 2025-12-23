package com.example.flashcardexpress.feature.questionManagement.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.flashcardexpress.feature.home.navigation.HomeScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryViewModel


fun NavGraphBuilder.setupQuestionManagementNavigation(navController: NavController) {


    composable(
    route= QuestionManagementScreen.CreationCategory.route
    ) {
        val viewModel: CreationCategoryViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        handleQuestionManagementNavigationEvents(viewModel, navController)
        CreationCategoryScreen(state,viewModel.effect,viewModel::onEvent)

    }


}

@Composable
private fun handleQuestionManagementNavigationEvents(
    viewModel: CreationCategoryViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { effect ->
            when (effect) {
                is CreationCategoryNavEffect.NavigateBackToCreationMenuNav -> {
                    handleNavigateToCreationMenu(navController)
                }
            }
        }

    }
}

private fun handleNavigateToCreationMenu(navController: NavController) {
    navController.navigate(HomeScreen.CreationMenu.route) {
        popUpTo(QuestionManagementScreen.CreationCategory.route) {
            inclusive = true
        }
    }
}