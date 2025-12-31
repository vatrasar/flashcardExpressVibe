package com.example.flashcardexpress.feature.home.presentation.creationMenu


sealed class CreationMenuEvent {

    data object OnNavigateToQuestionCreation: CreationMenuEvent()
    data object OnNavigateToCategoryCreation: CreationMenuEvent()
    data object OnBackToHome: CreationMenuEvent()

}

sealed class CreationMenuEffect{

    data object NavigateToQuestionCreation: CreationMenuEffect()
    data object NavigateToCategoryCreation: CreationMenuEffect()
    data object NavigateToHomeMenu: CreationMenuEffect()





}