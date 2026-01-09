package com.example.flashcardexpress.common.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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