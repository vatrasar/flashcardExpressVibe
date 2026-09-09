package com.example.flashcardexpress.feature.statistics.domain.model

data class GlobalStatistics(
    val totalWordsCount: Int,
    val learnedWordsCount: Int,
    val activeWordsCount: Int,
    val masteryPercentage: Int,
    val wordsReadyForTodayCount: Int,
    val stageStatistics: StageStatistics,
    val categoriesStatistics: List<CategoryStatistics>
)
