package com.dangxuanthong.youtube_ai_summarize.ui

import com.dangxuanthong.youtube_ai_summarize.network.ApiResponse
import com.dangxuanthong.youtube_ai_summarize.network.client
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.core.annotation.Factory

@Factory
class MainViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onUrlChange(newUrl: String) {
        _uiState.update { it.copy(videoId = newUrl) }
    }

    fun onGetTranscript() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(status = Status.Loading) }

            _uiState.update { uiState ->
                try {
                    val response = client.get {
                        url {
                            protocol = URLProtocol.HTTPS
                            host = "youtube-transcript-api1.vercel.app"
                            path("get-transcript")
                            parameters.append("video_id", uiState.videoId)
                        }
                    }.body<ApiResponse>()

                    when (response) {
                        is ApiResponse.Success ->
                            uiState.copy(status = Status.Success(result = response.data))

                        is ApiResponse.Error ->
                            uiState.copy(status = Status.Error(message = response.message))
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) return@update uiState.copy(status = Status.Idle)
                    e.printStackTrace()
                    uiState.copy(status = Status.Error(e.message as String))
                }
            }
        }
    }
}

data class UiState(
    val videoId: String = "",
    val status: Status = Status.Idle,
)

sealed interface Status {
    data object Idle : Status
    data object Loading : Status
    data class Success(val result: String) : Status
    data class Error(val message: String) : Status
}