package com.example.flashcardexpress.feature.questionManagement.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.flashcardexpress.feature.home.navigation.HomeFeatureScreen
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen.*
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit.CategoryEditNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit.CategoryEditScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit.CategoryEditViewModel

import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion.CreationQuestionNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion.CreationQuestionScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion.CreationQuestionViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit.QuestionEditNavEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit.QuestionEditScreen
import com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit.QuestionEditViewModel
import com.example.flashcardexpress.navigation.Screen


fun NavGraphBuilder.setupQuestionManagementNavigation(navController: NavController) {


    composable<QuestionManagementScreen.CreationCategory> {
        val viewModel: CreationCategoryViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        handleCategoryCreationNavigationEvents(viewModel, navController)
        CreationCategoryScreen(state,viewModel.effect,viewModel::onEvent)

    }
    composable<QuestionManagementScreen.ManagePanel>{
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
    composable<QuestionManagementScreen.CategoryEdit> {
        val viewModel: CategoryEditViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleCategoryEditNavigationEvents(viewModel,navController)
        CategoryEditScreen(state, viewModel::onEvent, viewModel.effect)

    }

    composable<QuestionManagementScreen.CreationQuestion> {
        val viewModel: CreationQuestionViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleQuestionCreationNavigationEvents(viewModel, navController)
        CreationQuestionScreen(state,viewModel::onEvent,viewModel.effect)
    }

    composable<QuestionManagementScreen.QuestionEdit> {
        val viewModel: QuestionEditViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(viewModel.navEffect)
        {
            viewModel.navEffect.collect {
                when(it)
                {
                    QuestionEditNavEffect.BackToCategoryDetails -> {
                        navController.popBackStack()
                    }
                }
            }
        }
        QuestionEditScreen(viewModel::onEvent,viewModel.effect,state)


    }


}

@Composable
private fun HandleQuestionCreationNavigationEvents(
    viewModel: CreationQuestionViewModel,
    navController: NavController)
{
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { effect ->
            when (effect) {
                CreationQuestionNavEffect.NavigateBackToCreationMenu -> {
                    navController.popBackStack()
                }
            }
        }
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
                is CreationCategoryNavEffect.NavigateBackToManagePanel -> {
                    handleNavigateBackToManagePanel(navController)
                }
            }
        }

    }
}

@Composable
private fun HandleCategoryEditNavigationEvents(
    viewModel: CategoryEditViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { effect ->
            when (effect) {
                CategoryEditNavEffect.BackToCategoryDetails -> {
                    navController.popBackStack()
                }

                is CategoryEditNavEffect.BackToCategoryDetailsAfterUpdate -> {

                    navController.navigate(QuestionManagementScreen.CategoryDetails(effect.categoryId,effect.categoryName))
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

                is CategoryDetailsNavEffect.NavigateToCategoryEdit -> {
                    navController.navigate(CategoryEdit(effect.categoryId,effect.categoryName))
                }

                is CategoryDetailsNavEffect.NavigateToQuestionCreation -> {

                    navController.navigate(CreationQuestion(effect.categoryId))
                }

                is CategoryDetailsNavEffect.NavigateToQuestionEdit -> {
                    navController.navigate(QuestionEdit(effect.questionId))
                }
            }
        }
    }
}


private fun handleBackNavToManagePanel(navController: NavController) {
    navController.navigate(QuestionManagementScreen.ManagePanel)
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

private fun handleNavigateBackToManagePanel(navController: NavController) {
    navController.navigate(HomeFeatureScreen.ManagePanel.route) {
        popUpTo<QuestionManagementScreen.CreationCategory> {
            inclusive = true
        }
    }
}