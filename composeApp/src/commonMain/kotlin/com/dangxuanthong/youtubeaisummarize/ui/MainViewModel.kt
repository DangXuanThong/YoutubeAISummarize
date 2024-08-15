package com.dangxuanthong.youtubeaisummarize.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
import com.dangxuanthong.youtubeaisummarize.network.client
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    fun onUrlChange(newUrl: String) {
        uiState = uiState.copy(url = newUrl)
    }

    fun onGetTranscript() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, result = null)
            uiState = try {
                val response = client.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "youtube-transcript-api1.vercel.app"
                        path("get-transcript")
                        parameters.append("video_id", uiState.url)
                    }
                }.body<ApiResponse>()

                when (response) {
                    is ApiResponse.Success -> uiState.copy(
                        isLoading = false,
                        result = response.data
                    )

                    is ApiResponse.Error -> uiState.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.copy(isLoading = false, error = e.message)
            }
        }
    }
}

data class UiState(
    val url: String = "",
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
)
