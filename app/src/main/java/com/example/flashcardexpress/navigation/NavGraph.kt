package com.example.flashcardexpress.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.flashcardexpress.feature.home.navigation.setupHomeNavigation


@Composable
fun SetupNavGraph(navController: NavHostController){

    NavHost(navController = navController, startDestination = Screen.Home.route)
    {
        setupHomeNavigation(navController)
    }
}

