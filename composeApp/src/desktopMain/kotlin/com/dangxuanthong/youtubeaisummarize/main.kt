@file:Suppress("ktlint:standard:filename")

package com.dangxuanthong.youtubeaisummarize

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dangxuanthong.composeapp.generated.resources.Res
import com.dangxuanthong.composeapp.generated.resources.app_name
import com.dangxuanthong.youtubeaisummarize.di.AppModule
import com.dangxuanthong.youtubeaisummarize.ui.App
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    startKoin {
        modules(AppModule().module)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name)
    ) {
        App(Modifier.padding(16.dp))
    }
}
