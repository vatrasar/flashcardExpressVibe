package com.example.flashcardexpress.common.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A standard confirmation dialog for critical or destructive user actions.
 *
 * Purpose:
 * Forces the user to confirm an action (e.g., deletion) to prevent accidental data loss.
 *
 * Usage (Inputs/Outputs/State):
 * - [onConfirm]: Callback function triggered when the user clicks the "OK" button.
 * - [onDismiss]: Callback function triggered when the user clicks the "Cancel" button or dismisses the dialog.
 * - [text]: The confirmation message to be displayed in the dialog body.
 *
 * Key UI elements:
 * - [AlertDialog]: The material dialog container.
 * - [TextButton]: Buttons for "OK" (confirm) and "Cancel" (dismiss).
 *
 * Used In:
 * - [CategoryDetailsScreen]
 */
@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    text:String)
{
    AlertDialog(
        onDismissRequest = { },
        text = { Text(text) },
        confirmButton ={
            TextButton(onClick = {
                onDismiss()
                onConfirm()

            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick =  onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}