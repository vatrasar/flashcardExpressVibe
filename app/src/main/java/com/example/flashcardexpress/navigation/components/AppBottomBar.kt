package com.example.flashcardexpress.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.flashcardexpress.navigation.BottomNavItem
import com.example.flashcardexpress.navigation.Screen

@Composable
fun AppBottomBar(
    currentDestination: NavDestination?,
    onNavigateToDestination: (Any) -> Unit,
    modifier: Modifier = Modifier
)
{
    val items = BottomNavItem.getAll()
    NavigationBar() {
        for (item in items) {
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.destination::class)
            } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateToDestination(item.destination) },
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = ""

                    ) },
                label = { Text( text=item.title) }
            )
        }




    }


}