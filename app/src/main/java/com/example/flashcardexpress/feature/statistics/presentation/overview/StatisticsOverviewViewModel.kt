package com.example.flashcardexpress.feature.statistics.presentation.overview

import androidx.lifecycle.viewModelScope
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.statistics.domain.usecase.GetGlobalStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsOverviewViewModel @Inject constructor(
    private val getGlobalStatisticsUseCase: GetGlobalStatisticsUseCase
) : BaseScreenAndNavEffectsViewModel<StatisticsOverviewEffect, StatisticsOverviewNavEffect>() {

    private val _state = MutableStateFlow(StatisticsOverviewState())
    val state: StateFlow<StatisticsOverviewState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    fun onEvent(event: StatisticsOverviewEvent) {
        when (event) {
            is StatisticsOverviewEvent.OnCategoryClicked -> {
                sendNavEffect(StatisticsOverviewNavEffect.NavigateToCategoryDetail(event.categoryId))
            }
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            getGlobalStatisticsUseCase().collect { globalStats ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    globalStatistics = globalStats
                )
            }
        }
    }
}
