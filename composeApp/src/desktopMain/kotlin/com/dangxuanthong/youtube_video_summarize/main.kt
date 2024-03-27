package com.dangxuanthong.youtube_video_summarize

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.youtube_video_summarize.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Youtube Video Summarize"
    ) {
        App()
    }
}
