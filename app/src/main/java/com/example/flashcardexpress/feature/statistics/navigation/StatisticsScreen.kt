package com.example.flashcardexpress.feature.statistics.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface StatisticsScreen {
    @Serializable
    data object Overview : StatisticsScreen

    @Serializable
    data class CategoryDetail(val categoryId: Int) : StatisticsScreen
}
