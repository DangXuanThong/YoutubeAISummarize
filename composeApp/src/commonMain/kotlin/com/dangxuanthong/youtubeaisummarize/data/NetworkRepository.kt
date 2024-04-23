package com.dangxuanthong.youtubeaisummarize.data

import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
import com.dangxuanthong.youtubeaisummarize.network.YTApiService
import org.koin.core.annotation.Single

interface NetworkRepository {
    suspend fun getTranscriptForVideo(videoId: String): ApiResponse
}

@Single
class DefaultNetworkRepository(private val ytApiService: YTApiService) : NetworkRepository {

    override suspend fun getTranscriptForVideo(videoId: String): ApiResponse {
        return ytApiService.getTranscriptForVideo(videoId)
    }
}
