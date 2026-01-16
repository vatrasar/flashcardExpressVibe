package com.example.flashcardexpress.feature.repeat.presentation.repeatPanel

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle
import com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionNavEffect

sealed class RepeatPanelEffect {
    data class ShowSnackbar(val message:String): RepeatPanelEffect()

}

sealed class RepeatPanelNavEffect {
    data class NavigateToRepetition(val categoryId:Int, val numberOfQuestionsInRepetition:Int): RepeatPanelNavEffect()

}

sealed class RepeatPanelEvent {

    data class OnRepetitionClicked(val categoryId:Int, val numberOfQuestionsInRepetition:Int):RepeatPanelEvent()

}


@Immutable
data class RepeatPanelState(
    val listOfRepetitions: List<ElementForListWithTitle>,
    val totalNumberOfQuestions:Int
)