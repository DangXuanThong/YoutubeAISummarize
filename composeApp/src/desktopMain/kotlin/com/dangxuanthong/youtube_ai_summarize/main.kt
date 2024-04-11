package com.dangxuanthong.youtube_ai_summarize

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.youtube_ai_summarize.di.commonModules
import com.dangxuanthong.youtube_ai_summarize.ui.App
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(commonModules)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Youtube Video Summarize"
    ) {
        App()
    }
}