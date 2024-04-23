package com.dangxuanthong.youtubeaisummarize.network

import com.dangxuanthong.youtubeaisummarize.BuildConfig
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.BlockThreshold
import dev.shreyaspatil.ai.client.generativeai.type.HarmCategory
import dev.shreyaspatil.ai.client.generativeai.type.SafetySetting

fun generateGenerativeModel() = GenerativeModel(
    modelName = "gemini-1.0-pro",
    apiKey = BuildConfig.GENAI_KEY,
    safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)
    )
)
