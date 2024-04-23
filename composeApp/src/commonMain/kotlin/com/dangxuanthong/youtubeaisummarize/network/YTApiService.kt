package com.dangxuanthong.youtubeaisummarize.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.path
import java.net.UnknownHostException
import org.koin.core.annotation.Single

interface YTApiService {
    suspend fun getTranscriptForVideo(videoId: String): ApiResponse
}

@Single
class DefaultYTApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://youtube-transcript-api1.vercel.app"
) : YTApiService {

    override suspend fun getTranscriptForVideo(videoId: String): ApiResponse =
        try {
            httpClient.get(baseUrl) {
                url {
                    path("get-transcript")
                    parameters.append("video_id", videoId)
//                    parameters.append("lang", "vi")
                }
            }.body()
        } catch (e: UnknownHostException) {
            ApiResponse.Error("No internet connection")
        } catch (e: HttpRequestTimeoutException) {
            ApiResponse.Error(e.message as String)
        }
}
