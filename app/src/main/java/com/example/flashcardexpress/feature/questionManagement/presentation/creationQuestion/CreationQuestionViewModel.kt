package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.flashcardexpress.common.viewModel.BaseEffectViewModel


@HiltViewModel
class CreationQuestionViewModel @Inject constructor() : BaseEffectViewModel<CreationQuestionEffect>(){

    public fun onEvent(event: CreationQuestionEvent)
    {

    }
}