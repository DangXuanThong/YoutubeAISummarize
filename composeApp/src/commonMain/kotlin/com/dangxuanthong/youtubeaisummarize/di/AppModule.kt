package com.dangxuanthong.youtubeaisummarize.di

import com.dangxuanthong.youtubeaisummarize.network.YTResponseSerializer
import com.dangxuanthong.youtubeaisummarize.network.generateGenerativeModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.dangxuanthong.youtubeaisummarize")
class AppModule {

    @Single
    fun provideHttpClient() = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    classDiscriminator = "result"
                    serializersModule = SerializersModule {
                        contextual(YTResponseSerializer)
                    }
                }
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    @Single
    fun provideGenerativeModel() = generateGenerativeModel()
}
