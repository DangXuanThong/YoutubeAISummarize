package com.dangxuanthong.youtubeaisummarize.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

@Serializable
data class YTResponse(
    val id: String,
    val thumbnailUrl: String,
    val defaultLanguage: String?
)

object YTResponseSerializer :
    JsonTransformingSerializer<YTResponse>(YTResponse.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement = buildJsonObject {
        val videoInfo = (element as JsonObject)["items"]!!.jsonArray[0].jsonObject
        val videoSnippet = videoInfo["snippet"]!!.jsonObject

        val videoId = videoInfo["id"]!!
        val thumbnailUrl = videoSnippet["thumbnails"]!!
            .jsonObject["maxres"]!!
            .jsonObject["url"]!!
        val defaultLanguage =
            videoSnippet["defaultLanguage"] ?: videoSnippet["defaultAudioLanguage"] ?: JsonNull

        put("id", videoId)
        put("thumbnailUrl", thumbnailUrl)
        put("defaultLanguage", defaultLanguage)
    }
}
