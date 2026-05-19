package com.example.flashcardexpress.feature.questionManagement.presentation.components.CategoryCreationForm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R.string
import com.example.flashcardexpress.common.theme.AppDimensions

/**
 * Reusable form for entering category details.
 *
 * Purpose:
 * Provides a consistent UI for both creating new categories and editing existing ones.
 *
 * Usage (Inputs/Outputs/State):
 * - [inputContent]: Current value of the category name.
 * - [selectedLanguage]: Current value of the selected category language.
 * - [pageTitle]: Title to be displayed at the top of the form.
 * - [categoryFormActions]: Set of callbacks for name changes, language changes, saving, and navigating back.
 *
 * Key UI elements:
 * - [OutlinedTextField] for category name input.
 * - [LanguageDropdownSelector] spinner for selecting the language.
 * - "Save" button to trigger the category saving logic.
 * - "Back" button to return to the previous screen.
 *
 * Used In:
 * - [CreationCategoryScreen]
 * - [CategoryEditScreen]
 */
@Composable
public fun CategoryCreationForm(
    inputContent: String,
    selectedLanguage: String,
    languages: List<String>,
    pageTitle: String,
    categoryFormActions: CategoryFormActions
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = AppDimensions.marginHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FormContent(inputContent, selectedLanguage, languages, pageTitle, categoryFormActions)
    }
}

@Composable
private fun FormContent(
    inputContent: String,
    selectedLanguage: String,
    languages: List<String>,
    pageTitle: String,
    categoryFormActions: CategoryFormActions
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleAndFormInput(
            inputContent = inputContent,
            selectedLanguage = selectedLanguage,
            languages = languages,
            pageTitle = pageTitle,
            onValueChange = categoryFormActions.onCategoryNameChanged,
            onLanguageChanged = categoryFormActions.onLanguageChanged
        )
    }

    Column {
        Buttons(categoryFormActions)
    }
}

@Composable
private fun Buttons(
    categoryFormActions: CategoryFormActions
) {
    Button(
        onClick = { categoryFormActions.onCategorySave() },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("categorySaveButton"),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.add_category_btn_text))
    }
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        onClick = { categoryFormActions.onBack() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("backButton"),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(stringResource(string.btn_back))
    }
    Spacer(modifier = Modifier.height(AppDimensions.marginBottom))
}

@Composable
private fun TitleAndFormInput(
    inputContent: String,
    selectedLanguage: String,
    languages: List<String>,
    pageTitle: String,
    onValueChange: (String) -> Unit,
    onLanguageChanged: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = pageTitle,
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        onValueChange = { onValueChange(it) },
        value = inputContent,
        label = { Text(stringResource(string.add_category_label)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("categoryNameTextField")
    )
    Spacer(modifier = Modifier.height(20.dp))
    LanguageDropdownSelector(
        selectedLanguage = selectedLanguage,
        languages = languages,
        onLanguageChanged = onLanguageChanged
    )
}

@Composable
private fun LanguageDropdownSelector(
    selectedLanguage: String,
    languages: List<String>,
    onLanguageChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Language",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("languageDropdownButton"),
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedLanguage)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select language"
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("languageDropdownMenu")
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = {
                            onLanguageChanged(language)
                            expanded = false
                        },
                        modifier = Modifier.testTag("languageOption_$language")
                    )
                }
            }
        }
    }
}