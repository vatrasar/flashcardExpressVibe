package com.example.flashcardexpress.feature.home.presentation.homeMenu
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.flashcardexpress.common.BaseEffectViewModel


@HiltViewModel
class HomeViewModel @Inject constructor() : BaseEffectViewModel<HomeEffect>(){

    public fun onEvent(event: HomeEvent)
    {
        when(event)
        {
            HomeEvent.NavigateToCreationMenu -> {
                sendEffect(HomeEffect.NavigateToCreationMenu)

            }
        }
    }


}