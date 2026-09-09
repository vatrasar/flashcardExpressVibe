package com.example.flashcardexpress.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flashcardexpress.R
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen

sealed class BottomNavItem(
    val destination: Any,
    val icon: ImageVector,
    @StringRes val titleRes: Int
) {
    data object Manage: BottomNavItem(
        destination = QuestionManagementScreen.ManagePanel,
        icon = Icons.Default.Create,
        titleRes = R.string.bottom_nav_manage
    )
    data object Repeat: BottomNavItem(
        destination = RepeatScreen.RepeatPanel(false),
        icon = Icons.Default.DateRange,
        titleRes = R.string.bottom_nav_repeat
    )
    companion object {
        fun getAll() = listOf(Manage, Repeat)
    }
}