package com.dangxuanthong.youtubeaisummarize.data

import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
import com.dangxuanthong.youtubeaisummarize.network.YTApiService
import com.dangxuanthong.youtubeaisummarize.network.YTResponse
import org.koin.core.annotation.Single

interface NetworkRepository {
    suspend fun getTranscriptForVideo(videoId: String, language: String): ApiResponse
    suspend fun getVideoDetail(videoId: String): YTResponse
}

@Single
class DefaultNetworkRepository(private val ytApiService: YTApiService) : NetworkRepository {

    override suspend fun getTranscriptForVideo(videoId: String, language: String): ApiResponse =
        ytApiService.getTranscriptForVideo(videoId, language)

    override suspend fun getVideoDetail(videoId: String): YTResponse =
        ytApiService.getVideoDetail(videoId)
}
