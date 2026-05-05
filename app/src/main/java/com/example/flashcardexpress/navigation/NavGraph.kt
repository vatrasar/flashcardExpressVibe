package com.example.flashcardexpress.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flashcardexpress.feature.questionManagement.navigation.QuestionManagementScreen
import com.example.flashcardexpress.feature.questionManagement.navigation.setupQuestionManagementNavigation
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.QuestionCreationForm
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import com.example.flashcardexpress.feature.repeat.navigation.setupRepeatNavigation
import com.example.flashcardexpress.navigation.components.AppBottomBar


@Composable
fun SetupNavGraph(navController: NavHostController){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = remember(currentDestination) {

        currentDestination?.hierarchy?.any { destination ->
            destination.hasRoute(QuestionManagementScreen.ManagePanel::class)||
                    destination.hasRoute(RepeatScreen.RepeatPanel::class)

        } == true
    }
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically {it},
                exit = slideOutVertically { it }
            ) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    onNavigateToDestination ={route->
                        navController.navigate(route){
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true

                        }

                    }
                )
            }
        }
    ) {innerPadding->





        NavHost(
            navController = navController,
            startDestination = QuestionManagementScreen.ManagePanel,
            modifier= Modifier.padding(innerPadding)
        )
        {
            setupQuestionManagementNavigation(navController)
            setupRepeatNavigation(navController)
        }

    }




}

