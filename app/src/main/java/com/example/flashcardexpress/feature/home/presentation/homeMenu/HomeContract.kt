package com.example.flashcardexpress.feature.home.presentation.homeMenu

sealed class HomeEffect{
    data object NavigateToCreationMenu: HomeEffect()
}

sealed class HomeEvent{
    data object NavigateToCreationMenu: HomeEvent()
}