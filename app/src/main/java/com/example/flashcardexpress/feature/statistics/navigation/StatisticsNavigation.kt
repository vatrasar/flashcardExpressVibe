package com.example.flashcardexpress.feature.statistics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsNavEffect
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsScreen
import com.example.flashcardexpress.feature.statistics.presentation.categoryDetail.CategoryStatisticsViewModel
import com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewNavEffect
import com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewScreen
import com.example.flashcardexpress.feature.statistics.presentation.overview.StatisticsOverviewViewModel

fun NavGraphBuilder.setupStatisticsNavigation(navController: NavController) {
    composable<StatisticsScreen.Overview> {
        val viewModel: StatisticsOverviewViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleNavEffectsFromOverview(viewModel, navController)
        StatisticsOverviewScreen(state, viewModel::onEvent, viewModel.effect)
    }

    composable<StatisticsScreen.CategoryDetail> {
        val viewModel: CategoryStatisticsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        HandleNavEffectsFromCategoryDetail(viewModel, navController)
        CategoryStatisticsScreen(state, viewModel::onEvent, viewModel.effect)
    }
}

@Composable
private fun HandleNavEffectsFromOverview(
    viewModel: StatisticsOverviewViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { navEffect ->
            when (navEffect) {
                is StatisticsOverviewNavEffect.NavigateToCategoryDetail -> {
                    navController.navigate(StatisticsScreen.CategoryDetail(navEffect.categoryId))
                }
            }
        }
    }
}

@Composable
private fun HandleNavEffectsFromCategoryDetail(
    viewModel: CategoryStatisticsViewModel,
    navController: NavController
) {
    LaunchedEffect(viewModel.navEffect) {
        viewModel.navEffect.collect { navEffect ->
            when (navEffect) {
                CategoryStatisticsNavEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
}
