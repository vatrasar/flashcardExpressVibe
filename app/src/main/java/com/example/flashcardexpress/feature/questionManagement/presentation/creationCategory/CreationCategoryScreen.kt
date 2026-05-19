package com.example.flashcardexpress.feature.questionManagement.presentation.creationCategory


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.flashcardexpress.R


import com.example.flashcardexpress.common.ui.components.flashcardSnackbar.FlashcardSnackbar
import com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm.CategoryCreationForm
import com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm.CategoryFormActions
import kotlinx.coroutines.flow.Flow


/**
 * Screen for creating a new flashcard category.
 *
 * Available Functionalities:
 * - Entering and validating a new category name.
 * - Saving the new category to the database.
 * - Navigating back to the previous screen.
 *
 * Key UI elements:
 * - [CategoryCreationForm]: The main form for category input.
 * - [FlashcardSnackbar]: Displays feedback messages (e.g., success or error).
 *
 * Navigation events exposed:
 * - [CreationCategoryEvent.OnBackToManagePanel]: Triggered when the user chooses to navigate back.
 *
 * Navigable from: [CreationMenuScreen].
 */
@Composable
fun CreationCategoryScreen(state: CreationCategoryState, effectFromViewModel: Flow<CreationCategoryEffect>, onEventFromViewModel:(CreationCategoryEvent)->Unit)
{
    val snackbarHostState = remember { SnackbarHostState() }

    handleEffectsFromViewModel(effectFromViewModel, snackbarHostState)
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)
        {data->
            FlashcardSnackbar(snackbarData = data)

        }},
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {

            val categoryFormActions= CategoryFormActions(
                onCategoryNameChanged = {onEventFromViewModel(CreationCategoryEvent.OnCategoryNameChanged(it))},
                onLanguageChanged = {onEventFromViewModel(CreationCategoryEvent.OnLanguageChanged(it))},
                onCategorySave =   {onEventFromViewModel(CreationCategoryEvent.OnAddCategoryClicked)},
                onBack = {onEventFromViewModel(CreationCategoryEvent.OnBackToManagePanel)}
            )
            CategoryCreationForm(
                inputContent = state.categoryName,
                selectedLanguage = state.language,
                pageTitle = stringResource(R.string.add_category_page_title),
                categoryFormActions = categoryFormActions
            )
        }
    }
}

@Composable
private fun handleEffectsFromViewModel(
    effectFromViewModel: Flow<CreationCategoryEffect>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is CreationCategoryEffect.ShowSnackbar -> {

                        snackbarHostState.showSnackbar(message = effect.message, actionLabel = effect.resultType,
                            duration = SnackbarDuration.Short)

                }
            }
        }

    }
}

