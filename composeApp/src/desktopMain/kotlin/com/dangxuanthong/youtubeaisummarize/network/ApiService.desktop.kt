package com.dangxuanthong.youtubeaisummarize.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideHttpClientEngine(): HttpClientEngineFactory<*> = OkHttp
