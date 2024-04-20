package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    uiState: UiState,
    onUrlChange: (String) -> Unit,
    onGetTranscript: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            TextField(
                value = uiState.url,
                onValueChange = onUrlChange,
                singleLine = true,
                keyboardActions = KeyboardActions(onGo = { onGetTranscript() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go)
            )
            Button(
                onClick = onGetTranscript,
                modifier = Modifier.padding(start = 8.dp).pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text(text = "Get Transcript")
            }
        }
        if (uiState.isLoading) LinearProgressIndicator()
        if (uiState.result != null) {
            Text(text = uiState.result, modifier = Modifier.verticalScroll(rememberScrollState()))
        }
    }
}
