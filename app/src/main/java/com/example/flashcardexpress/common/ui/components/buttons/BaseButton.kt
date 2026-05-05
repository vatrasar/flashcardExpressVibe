package com.example.flashcardexpress.common.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A basic button component with standardized styling and full-width behavior.
 *
 * Purpose:
 * Serves as a foundational button component to ensure UI consistency across the app.
 *
 * Usage (Inputs/Outputs/State):
 * - [btnText]: The text to be displayed inside the button.
 * - [onEventFromViewModel]: Callback function triggered when the button is clicked.
 * - [colors]: The color configuration for the button's container and content.
 *
 * Key UI elements:
 * - [Button]: The main clickable material component.
 * - [Text]: Displays the button's label.
 *
 * Used In:
 * - [CreationMenuScreen]
 * - [RepetitionScreen]
 */
@Composable
fun BaseButton(btnText:String,onEventFromViewModel: () -> Unit, colors: ButtonColors)
{
    Button(
        onClick = { onEventFromViewModel() }
        , colors = colors,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(btnText)
    }
}