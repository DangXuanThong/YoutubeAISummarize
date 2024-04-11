package com.dangxuanthong.youtube_ai_summarize.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val client = HttpClient(provideHttpClientEngine()) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            classDiscriminator = "result"
        })
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}

expect fun provideHttpClientEngine(): HttpClientEngineFactory<*>
