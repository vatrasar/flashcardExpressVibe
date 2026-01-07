package com.example.flashcardexpress.feature.questionManagement.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flashcardexpress.R
import com.example.flashcardexpress.feature.questionManagement.presentation.managePanel.ManagePanelEvent

@Composable
public fun PlusButtonOverList(onEventFromViewModel: () -> Unit,textId:Int) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {

        Button(
            onClick = {
                onEventFromViewModel()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )

        ) {
            Text(stringResource(textId))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }

    }
    Spacer(modifier = Modifier.height(20.dp))
}