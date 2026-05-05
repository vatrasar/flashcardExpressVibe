package com.example.flashcardexpress.common.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A button component designed for use in menu screens.
 *
 * Purpose:
 * Provides a standardized button for primary menu actions with the app's primary color scheme.
 *
 * Usage (Inputs/Outputs/State):
 * - [btnText]: The text to be displayed inside the button.
 * - [onEventFromViewModel]: Callback function triggered when the button is clicked.
 *
 * Key UI elements:
 * - [Button]: The main material button component with primary colors.
 * - [Text]: Displays the button's label.
 *
 * Used In:
 * - [HomeScreen]
 * - [CreationMenuScreen]
 */
@Composable
fun MenuButton(btnText:String,onEventFromViewModel: () -> Unit)
{
    Button(
        onClick = { onEventFromViewModel() }
        , colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(btnText)
    }
}