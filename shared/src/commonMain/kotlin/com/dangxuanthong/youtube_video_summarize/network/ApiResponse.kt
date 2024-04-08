package com.dangxuanthong.youtube_video_summarize.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ApiResponse {

    @Serializable
    @SerialName("success")
    data class Success(val data: String) : ApiResponse

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : ApiResponse
}