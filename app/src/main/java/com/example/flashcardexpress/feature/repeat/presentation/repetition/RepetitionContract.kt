package com.example.flashcardexpress.feature.repeat.presentation.repetition

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.repeat.domain.model.Flashcard

sealed class RepetitionEffect {




}

sealed class RepetitionNavEffect {
    data object BackToRepeatPanel : RepetitionNavEffect()
    data object BackToRepeatPanelAfterFinishingRepetition : RepetitionNavEffect()


}

sealed class RepetitionEvent {
    data object OnCancelRepetition:RepetitionEvent()
    data object OnAnswerCorrect:RepetitionEvent()
    data object OnAnswerIncorrect:RepetitionEvent()
    data object OnBackToQuestion:RepetitionEvent()
    data object OnShowAnswer:RepetitionEvent()
    data object OnPlayTtsClick:RepetitionEvent()



}


@Immutable
data class RepetitionState(
    val isAnswerPage: Boolean,
    val flashcard: Flashcard,
    val learningStage:Int,
    val categoryLanguage: String = "English"

)