package com.example.flashcardexpress.feature.home.presentation.homeMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.buttons.MenuButton

/**
 * Main menu screen of the application.
 *
 * Available Functionalities:
 * - Navigating to Creation Menu for adding new categories or questions.
 * - Navigating to Repeat Menu for learning sessions.
 * - Navigating to Manage Panel for viewing and editing existing flashcards.
 *
 * Key UI elements:
 * - List of [MenuButton] components representing main application sections.
 * - Headline text indicating the current screen.
 *
 * Navigation events exposed:
 * - [HomeEvent.NavigateToCreationMenu]: Triggered when the creation menu button is clicked.
 * - [HomeEvent.NavigateToManagePanel]: Triggered when the manage panel button is clicked.
 *
 * Navigable from: Main application navigation entry point.
 */
@Composable
fun HomeScreen(onEventFromViewModel:(HomeEvent)->Unit)
{

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MenuColumn(onEventFromViewModel);
    }


}



@Composable
private fun MenuColumn(operation:(event: HomeEvent)->Unit)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(AppDimensions.marginHorizontal)
    ) {

        MenuContent(operation);
    }
}

@Composable
private fun MenuContent(operation:(event: HomeEvent)->Unit)
{
    Text(
        text = "Menu",
        style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(16.dp))

    MenuButton(
        stringResource(R.string.btn_creation_menu)

    ) {
        operation(HomeEvent.NavigateToCreationMenu)
    }
    MenuButton(stringResource(R.string.btn_repeat_menu)

    ) {
        operation(HomeEvent.NavigateToCreationMenu)
    }
    MenuButton(
        stringResource(R.string.btn_manage_panel)

    ) {
        operation(HomeEvent.NavigateToManagePanel)
    }


}



@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onEventFromViewModel = {})
}