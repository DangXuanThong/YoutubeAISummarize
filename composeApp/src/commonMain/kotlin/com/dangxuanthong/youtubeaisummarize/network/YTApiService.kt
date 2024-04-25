package com.dangxuanthong.youtubeaisummarize.network

import com.dangxuanthong.youtubeaisummarize.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.path
import java.net.UnknownHostException
import org.koin.core.annotation.Single

interface YTApiService {
    suspend fun getTranscriptForVideo(videoId: String, lang: String): ApiResponse
    suspend fun getVideoDetail(videoId: String): YTResponse
}

@Single
class DefaultYTApiService(
    private val httpClient: HttpClient,
    private val transcriptUrl: String = "https://youtube-transcript-api1.vercel.app",
    private val ytApiUrl: String = "https://youtube.googleapis.com/youtube/v3/videos"
) : YTApiService {

    override suspend fun getTranscriptForVideo(videoId: String, lang: String): ApiResponse {
        return try {
            httpClient.get(transcriptUrl) {
                url {
                    path("get-transcript")
                    parameters.append("video_id", videoId)
                    parameters.append("lang", lang)
                }
            }.body()
        } catch (e: UnknownHostException) {
            ApiResponse.Error("No internet connection")
        } catch (e: HttpRequestTimeoutException) {
            ApiResponse.Error(e.message as String)
        }
    }

    override suspend fun getVideoDetail(videoId: String): YTResponse {
        return httpClient.get(ytApiUrl) {
            url {
                parameters.append("id", videoId)
                parameters.append("part", "snippet")
                parameters.append("key", BuildConfig.YT_API_KEY)
            }
        }.body()
    }
}
