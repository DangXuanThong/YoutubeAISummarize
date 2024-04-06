package com.dangxuanthong.youtube_video_summarize.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideHttpClientEngine(): HttpClientEngineFactory<*> = OkHttp
