package com.example.flashcardexpress.feature.questionManagement.presentation.questionEdit

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.questionManagement.presentation.components.QuestionCreationForm.CategorySelectOption
import com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion.CreationQuestionEffect

sealed class QuestionEditEffect {
    data class ShowSnackbar(val message: String, val resultType: String) : QuestionEditEffect()
}

sealed class QuestionEditNavEffect {
   data object BackToCategoryDetails: QuestionEditNavEffect()

}

sealed class QuestionEditEvent {
    object OnBackToCategoryDetailsClicked: QuestionEditEvent()
    data class OnWordChanged(val word: String) : QuestionEditEvent()
    data class OnTranslationChanged(val translation: String) : QuestionEditEvent()

    data object OnSaveQuestionClicked : QuestionEditEvent()



}


@Immutable
data class QuestionEditState(
    val word: String,
    val translation: String,

)