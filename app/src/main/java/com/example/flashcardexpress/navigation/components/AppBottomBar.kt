package com.example.flashcardexpress.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.flashcardexpress.navigation.BottomNavItem

/**
 * Bottom navigation bar for the application.
 *
 * Purpose:
 * Provides a persistent navigation interface for switching between the main sections of the app.
 *
 * Usage (Inputs/Outputs/State):
 * - [currentDestination]: The current navigation destination to determine which item is selected.
 * - [onNavigateToDestination]: Callback function triggered when a navigation item is clicked.
 * - [modifier]: Modifier to be applied to the navigation bar.
 *
 * Key UI elements:
 * - [NavigationBar]: Container for the navigation items.
 * - [NavigationBarItem]: Individual items representing navigation destinations with icons and labels.
 *
 * Used In:
 * - [com.example.flashcardexpress.navigation.SetupNavGraph]
 */
@Composable
fun AppBottomBar(
    currentDestination: NavDestination?,
    onNavigateToDestination: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = BottomNavItem.getAll()
    NavigationBar(modifier = modifier.testTag("appBottomBar")) {
        for (item in items) {
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.destination::class)
            } == true
            val title = stringResource(item.titleRes)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateToDestination(item.destination) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = title
                    )
                },
                label = { Text(text = title) },
                modifier = Modifier.testTag("bottomNavItem_${title.lowercase()}")
            )
        }
    }
}