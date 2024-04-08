package com.dangxuanthong.youtube_video_summarize.ui

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
                value = uiState.url,
                onValueChange = onUrlChange,
                singleLine = true,
                keyboardActions = KeyboardActions(onGo = {
                    softKeyboardController?.hide()
                    onGetTranscript()
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go)
            )
            Button(
                onClick = {
                    softKeyboardController?.hide()
                    onGetTranscript()
                },
                modifier = Modifier.padding(start = 8.dp).pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text(text = "Get Transcript", textAlign = TextAlign.Center)
            }
        }
        if (uiState.isLoading) LinearProgressIndicator()
        AnimatedVisibility(
            visible = uiState.result != null,
            enter = expandVertically(expandFrom = Alignment.CenterVertically),
            exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ) {
            uiState.result?.let {
                Text(
                    text = uiState.result,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
