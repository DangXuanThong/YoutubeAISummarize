package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dangxuanthong.composeapp.generated.resources.Res
import com.dangxuanthong.composeapp.generated.resources.fetching_video_info
import com.dangxuanthong.composeapp.generated.resources.summarize
import com.dangxuanthong.composeapp.generated.resources.summarizing
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            InputField(
                videoId = uiState.videoId,
                onVideoIdChange = onVideoIdChange,
                onSummarize = onSummarize
            )
            Spacer(Modifier.height(8.dp))
            Row {
                if (uiState.thumbnailUrl != null) {
                    CoilImage(
                        imageModel = { uiState.thumbnailUrl },
                        modifier = Modifier.fillMaxWidth(0.4f)
                            .aspectRatio(16f / 9)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                when (uiState.status) {
                    is Status.Success -> Text(
                        text = uiState.status.data,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                    is Status.Loading -> LoadingIndicator(uiState.status)
                    else -> {}
                }
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

@ExperimentalResourceApi
@OptIn(ExperimentalLayoutApi::class)
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
        OutlinedTextField(
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

@ExperimentalResourceApi
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
                val duration = 1_000
                val enter = slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(durationMillis = duration / 2, delayMillis = duration / 2)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = duration / 2, delayMillis = duration / 2)
                )
                val exit = slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(durationMillis = duration / 2)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = duration / 2)
                )

                (enter togetherWith exit).using(
                    SizeTransform { _, _ ->
                        snap(duration / 2)
                    }
                )
            },
            contentAlignment = Alignment.Center
        ) {
            when (it) {
                is Status.Loading.GetVideoInfo -> Text(
                    text = stringResource(Res.string.fetching_video_info)
                )
                is Status.Loading.Summarize -> Text(text = stringResource(Res.string.summarizing))
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
                Text(text = errorMessage)
            }
        }
    }
}
