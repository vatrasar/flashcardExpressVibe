package com.example.flashcardexpress.feature.questionManagement.presentation.categoryEdit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.flashcardexpress.R
import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm.CategoryCreationForm
import com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm.CategoryFormActions
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryEffect
import com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory.CreationCategoryEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun CategoryEditScreen(
    state: CategoryEditState,
    onEventFromViewModel: (CategoryEditEvent) -> Unit,
    effectFromViewModel: Flow<CategoryEditEffect>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleEffectsFromViewModel(effectFromViewModel, snackbarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
            { data ->
                FlashcardSnackbar(snackbarData = data)

            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            val categoryFormActions= CategoryFormActions(
                onCategoryNameChanged = {onEventFromViewModel(CategoryEditEvent.OnCategoryNameChanged(it))},
                onCategorySave =   {onEventFromViewModel(CategoryEditEvent.OnSaveCategoryClicked)},
                onBack = {onEventFromViewModel(CategoryEditEvent.OnBackToCategoryDetailsClicked)}
            )
            CategoryCreationForm(state.categoryName, stringResource(R.string.add_category_page_title),categoryFormActions)

        }
    }
}

@Composable
private fun HandleEffectsFromViewModel(
    effectFromViewModel: Flow<CategoryEditEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {

                is CategoryEditEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = effect.message, actionLabel = effect.resultType,
                        duration = SnackbarDuration.Short)
                }
            }

        }

    }
}

