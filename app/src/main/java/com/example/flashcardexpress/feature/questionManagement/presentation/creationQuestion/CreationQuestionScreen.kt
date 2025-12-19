package com.example.flashcardexpress.feature.questionManagement.presentation.creationQuestion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CreationQuestionScreen(onEventFromViewModel:(CreationQuestionEvent)->Unit)
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}

@Preview
@Composable
fun CreationQuestionScreenPreview() {
    CreationQuestionScreen(onEventFromViewModel = {})
}