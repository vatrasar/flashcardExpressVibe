package com.example.flashcardexpress.feature.home.presentation.creationMenu

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flashcardexpress.common.theme.AppDimensions
import com.example.flashcardexpress.common.ui.components.buttons.BackButton
import com.example.flashcardexpress.common.ui.components.buttons.BaseButton
import com.example.flashcardexpress.common.ui.components.buttons.MenuButton
import com.example.flashcardexpress.feature.home.presentation.homeMenu.HomeEvent


@Composable
fun CreationMenuScreen(onEventFromViewModel:(CreationMenuEvent)->Unit)
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MenuColumn(onEventFromViewModel);
    }

}

@Composable
private fun MenuColumn(onEventFromViewModel:(CreationMenuEvent)->Unit)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(AppDimensions.marginHorizontal)


    ) {
        MenuContent(onEventFromViewModel);

    }
}

@Composable
fun MenuContent(onEventFromViewModel: (CreationMenuEvent) -> Unit) {
    Text(
        text = "Menu",
        style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    MenuButton("Create question"
    ) {
        onEventFromViewModel(CreationMenuEvent.OnNavigateToQuestionCreation)

    }
    MenuButton("Create category"
    ) {
        onEventFromViewModel(CreationMenuEvent.OnNavigateToCategoryCreation)

    }


    BackButton{
        onEventFromViewModel(CreationMenuEvent.OnBackToHome)
    }
}


@Preview
@Composable
fun CreationMenuScreenPreview() {
    CreationMenuScreen(onEventFromViewModel = {})
}