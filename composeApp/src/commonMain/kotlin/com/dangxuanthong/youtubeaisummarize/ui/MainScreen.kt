package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dangxuanthong.composeapp.generated.resources.Res
import com.dangxuanthong.composeapp.generated.resources.summarize
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    uiState: UiState,
    onVideoIdChange: (String) -> Unit,
    onSummarize: () -> Unit,
    modifier: Modifier = Modifier
) {
    var shouldShowError by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.status) {
        if (uiState.status is Status.Error) {
            shouldShowError = true
            delay(3_000)
            shouldShowError = false
        }
    }

    Box(
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InputField(
                videoId = uiState.videoId,
                onVideoIdChange = onVideoIdChange,
                onSummarize = onSummarize,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            when (uiState.status) {
                is Status.Loading -> LoadingIndicator(uiState.status)
                is Status.Success -> Text(
                    text = uiState.status.data,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
                else -> {}
            }
        }

        if (uiState.status is Status.Error) {
            ErrorIndicator(
                showError = shouldShowError,
                errorMessage = uiState.status.message,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalLayoutApi::class)
@Composable
private fun InputField(
    videoId: String,
    onVideoIdChange: (String) -> Unit,
    onSummarize: () -> Unit,
    modifier: Modifier = Modifier
) {
    val softKeyboardController = LocalSoftwareKeyboardController.current

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = videoId,
            onValueChange = onVideoIdChange,
            singleLine = true,
            keyboardActions = KeyboardActions(onGo = {
                softKeyboardController?.hide()
                onSummarize()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go)
        )
        Button(
            onClick = {
                softKeyboardController?.hide()
                onSummarize()
            },
            modifier = Modifier.padding(8.dp).pointerHoverIcon(PointerIcon.Hand)
        ) {
            Text(
                text = stringResource(Res.string.summarize),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun LoadingIndicator(
    status: Status.Loading,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        LinearProgressIndicator()
        Spacer(Modifier.height(4.dp))
        AnimatedContent(
            targetState = status,
            transitionSpec = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) togetherWith
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            contentAlignment = Alignment.Center
        ) {
            when (it) {
                is Status.Loading.Transcript -> Text(text = "Getting transcript")
                is Status.Loading.Summarize -> Text(text = "Summarizing")
            }
        }
    }
}

@Composable
private fun ErrorIndicator(
    showError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = showError,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier
    ) {
        Surface(color = MaterialTheme.colorScheme.error) {
            Box(
                modifier = Modifier
                    .sizeIn(minWidth = 300.dp, minHeight = 50.dp, maxWidth = 600.dp)
                    .padding(16.dp, 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
