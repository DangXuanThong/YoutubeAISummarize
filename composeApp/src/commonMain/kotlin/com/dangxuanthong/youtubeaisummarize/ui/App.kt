package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dangxuanthong.youtubeaisummarize.ui.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.koin.compose.KoinContext

@Composable
fun App(modifier: Modifier = Modifier) {
    PreComposeApp {
        KoinContext {
            AppTheme {
                val vm: MainViewModel = koinViewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        uiState = vm.uiState.collectAsStateWithLifecycle().value,
                        onVideoIdChange = { vm.onVideoIdChange(it) },
                        onSummarize = { vm.onSummarize() },
                        modifier = modifier
                    )
                }
            }
        }
    }
}
