package com.example.flashcardexpress.feature.questionManagement.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.navigateAndPopUp
import com.example.flashcardexpress.feature.home.navigation.HomeFeatureScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsViewModel

import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelViewModel
import com.example.flashcardexpress.navigation.Screen


fun NavGraphBuilder.setupQuestionManagementNavigation(navController: NavController) {


    composable<QuestionManagementScreen.CreationCategory> {
        val viewModel: CreationCategoryViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        handleCategoryCreationNavigationEvents(viewModel, navController)
        CreationCategoryScreen(state,viewModel.effect,viewModel::onEvent)

    }
    composable(HomeFeatureScreen.ManagePanel.route){
        val viewModel: ManagePanelViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleManagePanelNavigationEvents(viewModel, navController)
        ManagePanelScreen(state,viewModel::onEvent,viewModel.effect)

    }
    composable<QuestionManagementScreen.CategoryDetails>{backStackEntry->


        val viewModel: CategoryDetailsViewModel= hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleCategoryDetailsNaviaitonEvents(viewModel,navController)
        CategoryDetailsScreen(state,viewModel::onEvent,viewModel.effect)


    }


}

@Composable
private fun handleCategoryCreationNavigationEvents(
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

@Composable
private fun HandleCategoryDetailsNaviaitonEvents(
    viewModel: CategoryDetailsViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { effect ->

            when (effect) {
                CategoryDetailsNavEffect.BackToManagePanel -> {
                    handleBackNavToManagePanel(navController)
                }
            }
        }
    }
}


private fun handleBackNavToManagePanel(navController: NavController) {
    navController.navigate(HomeFeatureScreen.ManagePanel.route)
    {
        popUpTo<QuestionManagementScreen.CategoryDetails>
        {
            inclusive = true
        }
    }
}

@Composable
private fun HandleManagePanelNavigationEvents(
    viewModel: ManagePanelViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { effect ->
            when(effect) {
                is ManagePanelNavEffect.BackToHome -> handleBackNavigationToMainMenu(navController)
                is ManagePanelNavEffect.NavigateCategoryDetails -> navController.navigate(
                    QuestionManagementScreen.CategoryDetails(effect.category.id,effect.category.name))
                ManagePanelNavEffect.NavigateToCategoryCreation -> {
                    navController.navigate(QuestionManagementScreen.CreationCategory)
                    {
                        popUpTo(HomeFeatureScreen.ManagePanel.route)
                        {
                            inclusive = true
                        }
                    }
                }

            }
        }


    }
}




private fun handleBackNavigationToMainMenu(navController: NavController) {
    navController.navigate(Screen.Home.route) {
        popUpTo(HomeFeatureScreen.ManagePanel.route){
            inclusive = true
        }
    }
}

private fun handleNavigateToCreationMenu(navController: NavController) {
    navController.navigate(HomeFeatureScreen.CreationMenu.route) {
        popUpTo<QuestionManagementScreen.CreationCategory> {
            inclusive = true
        }
    }
}