package com.example.flashcardexpress.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("home_screen")
    data object CreationMenu: Screen("creation_menu")

    data object QuestionCreation: Screen("question_creation")
}