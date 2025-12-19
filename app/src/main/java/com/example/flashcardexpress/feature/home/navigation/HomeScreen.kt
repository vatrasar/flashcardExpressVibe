package com.example.flashcardexpress.feature.home.navigation

import com.example.flashcardexpress.navigation.Screen

sealed class HomeScreen(val route: String) {
    data object CreationMenu: HomeScreen("creation_menu")

    data object QuestionCreation: HomeScreen("question_creation")
}