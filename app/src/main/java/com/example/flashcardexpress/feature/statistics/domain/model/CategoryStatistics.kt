package com.example.flashcardexpress.feature.statistics.domain.model

data class CategoryStatistics(
    val categoryId: Int,
    val categoryName: String,
    val language: String,
    val learnedWordsCount: Int,
    val activeWordsCount: Int,
    val totalWordsCount: Int,
    val masteryPercentage: Int,
    val stageStatistics: StageStatistics,
    val wordsReadyForTodayCount: Int
)
