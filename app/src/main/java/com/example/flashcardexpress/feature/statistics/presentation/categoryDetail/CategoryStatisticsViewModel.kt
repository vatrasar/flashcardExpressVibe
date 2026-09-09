package com.example.flashcardexpress.feature.statistics.presentation.categoryDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.statistics.domain.usecase.GetCategoryStatisticsUseCase
import com.example.flashcardexpress.feature.statistics.navigation.StatisticsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryStatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryStatisticsUseCase: GetCategoryStatisticsUseCase
) : BaseScreenAndNavEffectsViewModel<CategoryStatisticsEffect, CategoryStatisticsNavEffect>() {

    private val route = savedStateHandle.toRoute<StatisticsScreen.CategoryDetail>()
    val categoryId = route.categoryId

    private val _state = MutableStateFlow(CategoryStatisticsState())
    val state: StateFlow<CategoryStatisticsState> = _state.asStateFlow()

    init {
        loadCategoryStatistics()
    }

    fun onEvent(event: CategoryStatisticsEvent) {
        when (event) {
            CategoryStatisticsEvent.OnBackClicked -> {
                sendNavEffect(CategoryStatisticsNavEffect.NavigateBack)
            }
        }
    }

    private fun loadCategoryStatistics() {
        viewModelScope.launch {
            getCategoryStatisticsUseCase(categoryId).collect { catStats ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    categoryStatistics = catStats
                )
            }
        }
    }
}
