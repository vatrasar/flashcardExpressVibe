package com.example.flashcardexpress.feature.statistics.presentation.overview

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.core.domain.util.UiText
import com.example.flashcardexpress.feature.statistics.domain.model.GlobalStatistics

sealed class StatisticsOverviewEffect {
    data class ShowSnackbar(val message: UiText) : StatisticsOverviewEffect()
}

sealed class StatisticsOverviewNavEffect {
    data class NavigateToCategoryDetail(val categoryId: Int) : StatisticsOverviewNavEffect()
}

sealed class StatisticsOverviewEvent {
    data class OnCategoryClicked(val categoryId: Int) : StatisticsOverviewEvent()
}

@Immutable
data class StatisticsOverviewState(
    val isLoading: Boolean = true,
    val globalStatistics: GlobalStatistics? = null
)
