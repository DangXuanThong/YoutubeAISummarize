package com.dangxuanthong.youtubeaisummarize.ui

import com.dangxuanthong.youtubeaisummarize.data.AIRepository
import com.dangxuanthong.youtubeaisummarize.data.NetworkRepository
import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
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
class MainViewModel(
    private val networkRepository: NetworkRepository,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onVideoIdChange(newVideoId: String) {
        _uiState.update { it.copy(videoId = newVideoId) }
    }

    fun onSummarize() {
        // To prevent 2 requests run simultaneously
        if (uiState.value.status is Status.Loading) return

        viewModelScope.launch(Dispatchers.IO) {
            // Getting transcript
            _uiState.update { it.copy(status = Status.Loading.Transcript(it.videoId)) }
            getTranscript()
            // If there is an error in getting transcription, abort the process
            if (uiState.value.status is Status.Error) return@launch

            // Using AI to summarize the transcript of video
            val aiResponse = aiRepository.summarize(
                (uiState.value.status as Status.Loading.Summarize).transcription
            )
            _uiState.update {
                when (aiResponse) {
                    is ApiResponse.Success -> it.copy(status = Status.Success(aiResponse.data))
                    is ApiResponse.Error -> it.copy(status = Status.Error(aiResponse.message))
                }
            }
        }
    }

    private suspend fun getTranscript() {
        _uiState.update {
            try {
                val response = networkRepository.getTranscriptForVideo(it.videoId)
                when (response) {
                    is ApiResponse.Success ->
                        it.copy(status = Status.Loading.Summarize(response.data))

                    is ApiResponse.Error -> it.copy(status = Status.Error(response.message))
                }
            } catch (e: Exception) {
                if (e is CancellationException) return@update it.copy(status = Status.Idle)
                e.printStackTrace()
                it.copy(status = Status.Error(e.message as String))
            }
        }
    }
}

data class UiState(
    val videoId: String = "",
    val status: Status = Status.Idle
)

sealed interface Status {
    data object Idle : Status
    data class Error(val message: String) : Status
    data class Success(val data: String) : Status

    // To display different loading status accordingly
    interface Loading : Status {
        data class Transcript(val videoId: String) : Loading
        data class Summarize(val transcription: String) : Loading
    }
}
