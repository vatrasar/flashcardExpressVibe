package com.example.flashcardexpress.feature.statistics.presentation.categoryDetail

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.core.domain.util.UiText
import com.example.flashcardexpress.feature.statistics.domain.model.CategoryStatistics

sealed class CategoryStatisticsEffect {
    data class ShowSnackbar(val message: UiText) : CategoryStatisticsEffect()
}

sealed class CategoryStatisticsNavEffect {
    data object NavigateBack : CategoryStatisticsNavEffect()
}

sealed class CategoryStatisticsEvent {
    data object OnBackClicked : CategoryStatisticsEvent()
}

@Immutable
data class CategoryStatisticsState(
    val isLoading: Boolean = true,
    val categoryStatistics: CategoryStatistics? = null
)
