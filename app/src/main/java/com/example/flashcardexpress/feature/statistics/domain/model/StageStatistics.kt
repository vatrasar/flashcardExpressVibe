package com.example.flashcardexpress.feature.statistics.domain.model

data class StageStatistics(
    val stage0TodayOrNewCount: Int = 0,
    val stage1DayCount: Int = 0,
    val stage3DaysCount: Int = 0,
    val stage10DaysCount: Int = 0,
    val stage30DaysCount: Int = 0,
    val masteredCount: Int = 0
)
