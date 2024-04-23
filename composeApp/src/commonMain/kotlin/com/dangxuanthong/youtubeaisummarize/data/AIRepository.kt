package com.dangxuanthong.youtubeaisummarize.data

import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import org.koin.core.annotation.Single

interface AIRepository {
    suspend fun summarize(data: String): ApiResponse
}

@Single
class DefaultAIRepository(private val model: GenerativeModel) : AIRepository {

    override suspend fun summarize(data: String): ApiResponse {
        val response = model.generateContent("Summarize this: $data")
        val promptFeedback = response.promptFeedback

        return if (promptFeedback == null) {
            ApiResponse.Success(data = response.text!!)
        } else {
            ApiResponse.Error(
                message = promptFeedback.blockReason?.toString()
                    ?: "Unknown error. Please report this to developer."
            )
        }
    }
}
