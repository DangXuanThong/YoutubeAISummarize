@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dangxuanthong.youtubeaisummarize.ui.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
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
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}
