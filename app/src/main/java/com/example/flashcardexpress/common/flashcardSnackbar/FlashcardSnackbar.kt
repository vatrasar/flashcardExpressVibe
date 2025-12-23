package com.example.flashcardexpress.common.flashcardSnackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close

import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FlashcardSnackbar(snackbarData: SnackbarData)
{
    when(snackbarData.visuals.actionLabel)
    {
        SnackbarType.INFO.label -> InfoSnackbar(snackbarData)
        SnackbarType.SUCCESS.label -> SuccessSnackbar(snackbarData)
        SnackbarType.ERROR.label -> ErrorSnackbar(snackbarData)

    }




}

@Composable
private fun InfoSnackbar(snackbarData: SnackbarData) {
    val contentColor=MaterialTheme.colorScheme.onSurfaceVariant
    val icon=Icons.Default.Info
    val containerColor=MaterialTheme.colorScheme.surfaceContainer
    UnivarsalSnackbar(contentColor, containerColor,snackbarData, icon)
}


@Composable
private fun ErrorSnackbar(snackbarData: SnackbarData) {
    val contentColor = MaterialTheme.colorScheme.onErrorContainer
    val containerColor=MaterialTheme.colorScheme.errorContainer
    val icon = Icons.Default.Close

    UnivarsalSnackbar(contentColor,containerColor, snackbarData, icon)
}

@Composable
private fun SuccessSnackbar(snackbarData: SnackbarData) {
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val containerColor=MaterialTheme.colorScheme.primaryContainer
    val icon = Icons.Default.Check

    UnivarsalSnackbar(contentColor,containerColor, snackbarData, icon)
}


@Composable
private fun UnivarsalSnackbar(
    contentColor: Color,
    containerColor:Color,
    snackbarData: SnackbarData,
    icon: ImageVector
) {
    Snackbar(
        modifier = Modifier.padding(12.dp)
            .clickable{snackbarData.dismiss()},
        containerColor = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(16.dp),


    )
    {
        SnackbarContent(snackbarData, icon,contentColor)

    }
}


@Composable
private fun SnackbarContent(snackbarData: SnackbarData, icon: ImageVector,iconColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {

        //Spacer(modifier = Modifier.width(12.dp))
        Text(text = snackbarData.visuals.message)
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )


    }
}