package com.example.flashcardexpress.feature.home.presentation.homeMenu

sealed class HomeEffect{
    data object NavigateToCreationMenu: HomeEffect()
    data object NavigateToManagePanel: HomeEffect()
}

sealed class HomeEvent{
    data object NavigateToCreationMenu: HomeEvent()
    data object NavigateToManagePanel: HomeEvent()

}