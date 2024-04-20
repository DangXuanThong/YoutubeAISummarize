package com.dangxuanthong.youtubeaisummarize.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun provideHttpClientEngine(): HttpClientEngineFactory<*> = Js
