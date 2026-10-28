package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions

import androidx.compose.runtime.Immutable
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionWithLinkStatus

sealed class LinkQuestionsEffect

sealed class LinkQuestionsNavEffect {
    data object NavigateBack : LinkQuestionsNavEffect()
}

sealed class LinkQuestionsEvent {
    data class OnSearchQueryChanged(val query: String) : LinkQuestionsEvent()
    data class OnToggleLink(val targetQuestionId: Int, val isCurrentlyLinked: Boolean) : LinkQuestionsEvent()
    data object OnBackClicked : LinkQuestionsEvent()
}

@Immutable
data class LinkQuestionsState(
    val sourceWord: String = "",
    val searchQuery: String = "",
    val allQuestions: List<QuestionWithLinkStatus> = emptyList(),
    val filteredQuestions: List<QuestionWithLinkStatus> = emptyList(),
    val isLoading: Boolean = true
)
