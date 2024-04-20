@file:Suppress("ktlint:standard:filename")

package com.dangxuanthong.youtubeaisummarize

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.youtubeaisummarize.di.AppModule
import com.dangxuanthong.youtubeaisummarize.ui.App
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

fun main() = application {
    startKoin {
        modules(AppModule().module)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Youtube Video Summarize"
    ) {
        App()
    }
}
