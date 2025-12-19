package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.flashcardexpress.common.BaseEffectViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class CreationCategoryViewModel @Inject constructor() : BaseEffectViewModel<CreationCategoryEffect>(){

    private val _state = MutableStateFlow(CreationCategoryState(""))
    val state = _state.asStateFlow()

    public fun onEvent(event: CreationCategoryEvent)
    {
        when(event)
        {
            is CreationCategoryEvent.OnCategoryNameChanged -> updateStateAfterCategoryChanged(event)
            CreationCategoryEvent.OnAddCategoryClicked -> {throw  NotImplementedError()}
            CreationCategoryEvent.OnBackToCreationMenuClicked -> {
                sendEffect(CreationCategoryEffect.NavigateBackToCreationMenu)
            }

        }

    }

    private fun updateStateAfterCategoryChanged(event: CreationCategoryEvent.OnCategoryNameChanged)
    {
        _state.update { it.copy(categoryName = event.currentValue) }

    }


}