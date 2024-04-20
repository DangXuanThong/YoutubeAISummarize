package com.dangxuanthong.youtubeaisummarize.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android

actual fun provideHttpClientEngine(): HttpClientEngineFactory<*> = Android
