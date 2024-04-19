package com.dangxuanthong.youtube_ai_summarize

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.dangxuanthong.youtube_ai_summarize.di.commonModules
import com.dangxuanthong.youtube_ai_summarize.ui.App
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(commonModules)
    }

    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}
