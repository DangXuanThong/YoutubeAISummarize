@file:Suppress("ktlint:standard:filename")

package com.dangxuanthong.youtubeaisummarize

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.dangxuanthong.youtubeaisummarize.di.commonModules
import com.dangxuanthong.youtubeaisummarize.ui.App
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(commonModules)
    }

    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}
