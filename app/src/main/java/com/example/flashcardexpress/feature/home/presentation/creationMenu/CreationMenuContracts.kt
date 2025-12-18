package com.example.flashcardexpress.feature.home.presentation.creationMenu


sealed class CreationMenuEvent {

    data object OnNavigateToQuestionCreation: CreationMenuEvent()

}

sealed class CreationMenuEffect{

    data object NavigateToQuestionCreation: CreationMenuEffect()

}