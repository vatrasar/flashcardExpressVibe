package com.example.flashcardexpress.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import com.example.flashcardexpress.feature.repeat.presentation.repeatPanel.RepeatPanelScreen

sealed class BottomNavItem(
    val destination: Any,
    val icon: ImageVector,
    val title: String
)
{
    data object Manage: BottomNavItem(
        destination = QuestionManagementScreen.ManagePanel,
        icon = Icons.Default.Create,
        title = "manage"
    )
    data object Repeat: BottomNavItem(
        destination = RepeatScreen.RepeatPanel(false),
        icon=Icons.Default.DateRange,
        title = "repeat"
    )
    companion object {
        fun getAll() = listOf(Manage, Repeat)

    }
}