@file:Suppress("ktlint:standard:filename")

package com.dangxuanthong.youtubeaisummarize

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.youtubeaisummarize.di.commonModules
import com.dangxuanthong.youtubeaisummarize.ui.App
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
