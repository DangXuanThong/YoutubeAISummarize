package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dangxuanthong.youtubeaisummarize.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        AppTheme {
            val vm: MainViewModel = koinViewModel()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScreen(
                    uiState = vm.uiState,
                    onUrlChange = { vm.onUrlChange(it) },
                    onGetTranscript = { vm.onGetTranscript() },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
