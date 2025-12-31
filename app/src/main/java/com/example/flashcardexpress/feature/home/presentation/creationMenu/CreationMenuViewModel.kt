package com.example.flashcardexpress.feature.home.presentation.creationMenu

import com.example.flashcardexpress.common.viewModel.BaseEffectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreationMenuViewModel @Inject constructor() : BaseEffectViewModel<CreationMenuEffect>() {


    public fun onEvent(event: CreationMenuEvent)
    {
        when(event)
        {
            CreationMenuEvent.OnNavigateToQuestionCreation -> {
                sendEffect(CreationMenuEffect.NavigateToQuestionCreation)

            }

            CreationMenuEvent.OnNavigateToCategoryCreation -> {
                sendEffect(CreationMenuEffect.NavigateToCategoryCreation)

            }

            CreationMenuEvent.OnBackToHome -> sendEffect(CreationMenuEffect.NavigateToHomeMenu)
        }
    }
}