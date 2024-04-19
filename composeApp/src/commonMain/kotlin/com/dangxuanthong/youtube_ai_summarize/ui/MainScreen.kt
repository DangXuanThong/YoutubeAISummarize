package com.dangxuanthong.youtube_ai_summarize.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    uiState: UiState,
    onUrlChange: (String) -> Unit,
    onGetTranscript: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val softKeyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            TextField(
                value = uiState.videoId,
                onValueChange = onUrlChange,
                singleLine = true,
                keyboardActions = KeyboardActions(onGo = {
                    softKeyboardController?.hide()
                    onGetTranscript()
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    softKeyboardController?.hide()
                    onGetTranscript()
                },
                modifier = Modifier.padding(start = 8.dp).pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text(
                    text = "Get Transcript",
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
        if (uiState.status is Status.Loading) LinearProgressIndicator()
        AnimatedVisibility(
            visible = uiState.status is Status.Success,
            enter = expandVertically(expandFrom = Alignment.CenterVertically),
            exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ) {
            if (uiState.status is Status.Success) {
                Text(
                    text = uiState.status.result,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
