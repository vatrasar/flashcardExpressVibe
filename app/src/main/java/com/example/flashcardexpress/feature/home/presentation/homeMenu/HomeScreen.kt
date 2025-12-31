package com.example.flashcardexpress.feature.home.presentation.homeMenu

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcardexpress.R

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
        verticalArrangement = Arrangement.Center
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
        Text(stringResource(R.string.btn_creation_menu))
    }
    Button(
        onClick ={operation(HomeEvent.NavigateToCreationMenu)}
    ) {
        Text(stringResource(R.string.btn_repeat_menu))
    }
    Button(
        onClick ={operation(HomeEvent.NavigateToManagePanel)}
    ) {
        Text(stringResource(R.string.btn_manage_panel))
    }


}



@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onEventFromViewModel = {})
}