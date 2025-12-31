package com.example.flashcardexpress.feature.home.navigation

sealed class HomeFeatureScreen(val route: String) {
    data object CreationMenu: HomeFeatureScreen("creation_menu")

    data object QuestionCreation: HomeFeatureScreen("question_creation")
    data object ManagePanel: HomeFeatureScreen("manage_panel")
}