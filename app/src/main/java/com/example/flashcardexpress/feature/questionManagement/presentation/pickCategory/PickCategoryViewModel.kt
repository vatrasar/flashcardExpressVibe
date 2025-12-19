package com.example.flashcardexpress.feature.questionManagement.presentation.pickCategory

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.flashcardexpress.common.BaseEffectViewModel


@HiltViewModel
class PickCategoryViewModel @Inject constructor() : BaseEffectViewModel<PickCategoryEffect>() {

    public fun onEvent(event: PickCategoryEvent) {

    }
}