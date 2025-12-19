package com.example.flashcardexpress.feature.home.presentation.creationMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
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
        verticalArrangement = Arrangement.Center

    ) {
        MenuContent(onEventFromViewModel);

    }
}

@Composable
fun MenuContent(onEventFromViewModel: (CreationMenuEvent) -> Unit) {
    Button(
        onClick = { onEventFromViewModel(CreationMenuEvent.OnNavigateToQuestionCreation) }
    ) {
        Text("Create question")
    }
    Button(
        onClick = { onEventFromViewModel(CreationMenuEvent.OnNavigateToCategoryCreation) }
    ) {
        Text("Create category")
    }
}


@Preview
@Composable
fun CreationMenuScreenPreview() {
    CreationMenuScreen(onEventFromViewModel = {})
}