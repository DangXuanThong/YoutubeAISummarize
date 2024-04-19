package com.dangxuanthong.youtube_ai_summarize

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.youtube_ai_summarize.di.AppModule
import com.dangxuanthong.youtube_ai_summarize.ui.App
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
