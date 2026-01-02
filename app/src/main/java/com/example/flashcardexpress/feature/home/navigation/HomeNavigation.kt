package com.example.flashcardexpress.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.flashcardexpress.feature.home.presentation.creationMenu.CreationMenuEffect
import com.example.flashcardexpress.feature.home.presentation.creationMenu.CreationMenuScreen
import com.example.flashcardexpress.feature.home.presentation.creationMenu.CreationMenuViewModel
import com.example.flashcardexpress.feature.home.presentation.homeMenu.HomeEffect
import com.example.flashcardexpress.feature.home.presentation.homeMenu.HomeScreen
import com.example.flashcardexpress.feature.home.presentation.homeMenu.HomeViewModel
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.navigation.Screen



fun NavGraphBuilder.setupHomeNavigation(navController: NavController){


    composable(
        route= Screen.Home.route
    ){
        val viewModel: HomeViewModel= hiltViewModel()
        HandleHomeNavigationEvents(viewModel, navController)
        HomeScreen(viewModel::onEvent)

    }
    composable(
        route= HomeFeatureScreen.CreationMenu.route
    ) {
        val viewModel: CreationMenuViewModel= hiltViewModel()
        handleCreationMenuNavigationEvents(viewModel, navController)



        CreationMenuScreen(viewModel::onEvent)
    }



}


@Composable
private fun handleCreationMenuNavigationEvents(
    viewModel: CreationMenuViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CreationMenuEffect.NavigateToQuestionCreation -> navController.navigate(
                    QuestionManagementScreen.CreationQuestion)
                CreationMenuEffect.NavigateToCategoryCreation -> navController.navigate(
                    QuestionManagementScreen.CreationCategory)

                CreationMenuEffect.NavigateToHomeMenu -> {
                    HandleNavigationToHomeFromCreationMenu(navController)
                }
            }

        }
    }

}


private fun HandleNavigationToHomeFromCreationMenu(navController: NavController) {
    navController.navigate(Screen.Home.route)
    {
        popUpTo(HomeFeatureScreen.CreationMenu.route)
        {
            inclusive = true
        }
    }
}

@Composable
private fun HandleHomeNavigationEvents(
    viewModel: HomeViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToCreationMenu -> navController.navigate(HomeFeatureScreen.CreationMenu.route);
                HomeEffect.NavigateToManagePanel -> navController.navigate(HomeFeatureScreen.ManagePanel.route)

            }

        }
    }
}



