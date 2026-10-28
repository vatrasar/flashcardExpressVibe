package com.example.flashcardexpress.feature.repeat.presentation.linkQuestions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.flashcardexpress.common.viewModel.BaseScreenAndNavEffectsViewModel
import com.example.flashcardexpress.feature.repeat.domain.model.QuestionWithLinkStatus
import com.example.flashcardexpress.feature.repeat.domain.usecase.GetCategoryQuestionsWithLinkStatusUseCase
import com.example.flashcardexpress.feature.repeat.domain.usecase.ToggleQuestionLinkUseCase
import com.example.flashcardexpress.feature.repeat.navigation.RepeatScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkQuestionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCategoryQuestionsWithLinkStatus: GetCategoryQuestionsWithLinkStatusUseCase,
    private val toggleQuestionLink: ToggleQuestionLinkUseCase
) : BaseScreenAndNavEffectsViewModel<LinkQuestionsEffect, LinkQuestionsNavEffect>() {

    private val routeArgs = savedStateHandle.toRoute<RepeatScreen.LinkQuestions>()

    private val _state = MutableStateFlow(LinkQuestionsState(sourceWord = routeArgs.sourceWord))
    val state = _state.asStateFlow()

    init {
        loadCategoryQuestions()
    }

    fun onEvent(event: LinkQuestionsEvent) {
        when (event) {
            is LinkQuestionsEvent.OnSearchQueryChanged -> {
                updateSearchQuery(event.query)
            }
            is LinkQuestionsEvent.OnToggleLink -> {
                toggleLink(event.targetQuestionId, event.isCurrentlyLinked)
            }
            LinkQuestionsEvent.OnBackClicked -> {
                sendNavEffect(LinkQuestionsNavEffect.NavigateBack)
            }
        }
    }

    private fun loadCategoryQuestions() {
        viewModelScope.launch {
            val questions = getCategoryQuestionsWithLinkStatus(routeArgs.categoryId, routeArgs.questionId)
            _state.value = _state.value.copy(
                allQuestions = questions,
                filteredQuestions = applyFilter(questions, _state.value.searchQuery),
                isLoading = false
            )
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredQuestions = applyFilter(_state.value.allQuestions, query)
        )
    }

    private fun toggleLink(targetQuestionId: Int, isCurrentlyLinked: Boolean) {
        val newStatus = !isCurrentlyLinked
        val updatedAll = _state.value.allQuestions.map {
            updateQuestionStatus(it, targetQuestionId, newStatus)
        }
        _state.value = _state.value.copy(
            allQuestions = updatedAll,
            filteredQuestions = applyFilter(updatedAll, _state.value.searchQuery)
        )
        viewModelScope.launch {
            toggleQuestionLink(routeArgs.questionId, targetQuestionId, newStatus)
        }
    }

    private fun updateQuestionStatus(
        item: QuestionWithLinkStatus,
        targetId: Int,
        newStatus: Boolean
    ): QuestionWithLinkStatus {
        if (item.question.id == targetId) {
            return item.copy(isLinked = newStatus)
        }
        return item
    }

    private fun applyFilter(
        questions: List<QuestionWithLinkStatus>,
        query: String
    ): List<QuestionWithLinkStatus> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return questions
        }
        return questions.filter { matchesQuery(it, trimmedQuery) }
    }

    private fun matchesQuery(item: QuestionWithLinkStatus, query: String): Boolean {
        val wordMatches = item.question.word.contains(query, ignoreCase = true)
        val translationMatches = item.question.translation.contains(query, ignoreCase = true)
        return wordMatches || translationMatches
    }
}
