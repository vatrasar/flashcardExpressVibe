package com.example.flashcardexpress.feature.home.presentation.creationMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
//    MenuButton("Create category"
//    ) {
//        onEventFromViewModel(CreationMenuEvent.OnNavigateToCategoryCreation)
//
//    }

    Card(
        modifier = Modifier.fillMaxWidth()
            .height(180.dp)
        , colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

    ) {
        Column(
            verticalArrangement =Arrangement.Center
            , modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp).fillMaxWidth()

                ) {
                Text("Create category"
                ,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

        }

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