package com.example.flashcardexpress.common.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R

/**
 * A specialized button for navigating back to the previous screen.
 *
 * Purpose:
 * Provides a consistent "Back" action with predefined styling (secondary color scheme).
 *
 * Usage (Inputs/Outputs/State):
 * - [onEventFromViewModel]: Callback function triggered when the button is clicked.
 *
 * Key UI elements:
 * - [BaseButton]: The underlying button component configured with "Back" text and secondary colors.
 *
 * Used In:
 * - [CreationMenuScreen]
 * - [CategoryDetailsScreen]
 * - [ManagePanelScreen]
 */
@Composable
fun BackButton(onEventFromViewModel: () -> Unit)
{
    val colors= ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary

    )
     BaseButton(stringResource(R.string.btn_back),onEventFromViewModel,colors)
}