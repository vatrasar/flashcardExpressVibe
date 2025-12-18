package com.example.flashcardexpress.feature.home.presentation.homeMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.flashcardexpress.navigation.Screen

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuContent(operation);
    }
}

@Composable
private fun MenuContent(operation:(event: HomeEvent)->Unit)
{
    Button(
        onClick ={operation(HomeEvent.NavigateToCreationMenu)}
    ) {
        Text("no kliknij")
    }

}

