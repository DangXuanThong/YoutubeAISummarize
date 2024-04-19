package com.dangxuanthong.youtube_ai_summarize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dangxuanthong.youtube_ai_summarize.ui.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    PreComposeApp {
        KoinContext {
            AppTheme {
                val vm: MainViewModel = koinInject()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        uiState = vm.uiState.collectAsStateWithLifecycle().value,
                        onUrlChange = { vm.onUrlChange(it) },
                        onGetTranscript = { vm.onGetTranscript() },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
