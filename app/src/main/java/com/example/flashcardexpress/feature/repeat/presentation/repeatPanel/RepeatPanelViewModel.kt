package com.example.flashcardexpress.feature.repeat.presentation.repeatPanel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.questionManagement.presentation.categoryDetails.CategoryDetailsState
import com.example.flashcardexpress.feature.repeat.domain.model.ElementForListWithCount
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetAllRepetitionsForTodayUseCase
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class RepeatPanelViewModel @Inject constructor(getAllRepetitionsForToday: GetAllRepetitionsForTodayUseCase,savedStateHandle: SavedStateHandle) :
    BaseScreenAndNavEffectsViewModel<RepeatPanelEffect, RepeatPanelNavEffect>() {


    val state = getAllRepetitionsForToday().map { list->
        val listOfElemntsForLits=list.map {
            ElementForListWithCount(it.count,it.categoryName,it.categoryId)

        }.toList()
        val totalNumberOfQuestions=list.sumOf { it.count }

        RepeatPanelState(listOfElemntsForLits,totalNumberOfQuestions)


    }.stateIn(
        scope = viewModelScope,
        initialValue = RepeatPanelState(listOf(),0),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        val args=savedStateHandle.toRoute<RepeatScreen.RepeatPanel>()
        val isRepetitionFinished=args.isRepetitionFinished
        if (isRepetitionFinished)
        {
            sendEffect(RepeatPanelEffect.ShowSnackbar(
                "Repetition finished!"
                ))
        }


    }

    public fun onEvent(event: RepeatPanelEvent) {
        when (event) {
            is RepeatPanelEvent.OnRepetitionClicked -> sendNavEffect(RepeatPanelNavEffect.NavigateToRepetition(event.categoryId,event.numberOfQuestionsInRepetition))
        }

    }



}