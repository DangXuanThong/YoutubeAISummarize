package com.dangxuanthong.youtubeaisummarize.ui

import com.dangxuanthong.youtubeaisummarize.data.AIRepository
import com.dangxuanthong.youtubeaisummarize.data.NetworkRepository
import com.dangxuanthong.youtubeaisummarize.network.ApiResponse
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

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState("SRkdCkG9vJE"))
    val uiState = _uiState.asStateFlow()

    fun onVideoIdChange(newVideoId: String) {
        _uiState.update { it.copy(videoId = newVideoId) }
    }

    fun onSummarize() {
        // To prevent 2 requests run simultaneously
        if (uiState.value.status is Status.Loading) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    thumbnailUrl = null,
                    status = Status.Loading.GetVideoInfo(it.videoId)
                )
            }

            try {
                // Get video info
                val videoInfo = networkRepository.getVideoDetail(uiState.value.videoId)
                _uiState.update { it.copy(thumbnailUrl = videoInfo.thumbnailUrl) }

                // Getting transcript
                val transcriptStatus =
                    getTranscript(videoInfo.id, videoInfo.defaultLanguage ?: "en")
                if (transcriptStatus is Status.Error) return@launch
                updateStatus(transcriptStatus)

                // Summarize the transcript
                val summaryStatus =
                    summarize((transcriptStatus as Status.Loading.Summarize).transcription)
                if (summaryStatus is Status.Error) return@launch
                updateStatus(summaryStatus)
            } catch (e: IndexOutOfBoundsException) {
                updateStatus(Status.Error("Video not found"))
            } catch (e: Exception) {
                updateStatus(Status.Error(e.message.toString()))
            }
        }
    }

    private suspend fun getTranscript(videoId: String, language: String): Status {
        return when (val transcript = networkRepository.getTranscriptForVideo(videoId, language)) {
            is ApiResponse.Success -> Status.Loading.Summarize(transcript.data)
            is ApiResponse.Error -> Status.Error(transcript.message)
        }
    }

    private suspend fun summarize(transcript: String): Status {
        return when (val aiResponse = aiRepository.summarize(transcript)) {
            is ApiResponse.Success -> Status.Success(aiResponse.data)
            is ApiResponse.Error -> Status.Error(aiResponse.message)
        }
    }

    private fun updateStatus(newStatus: Status) {
        _uiState.update { it.copy(status = newStatus) }
    }
}

data class UiState(
    val videoId: String = "",
    val thumbnailUrl: String? = null,
    val status: Status = Status.Idle
)

sealed interface Status {
    data object Idle : Status
    data class Error(val message: String) : Status
    data class Success(val data: String) : Status

    // To display different loading status accordingly
    interface Loading : Status {
        data class GetVideoInfo(val videoId: String) : Loading
        data class Summarize(val transcription: String) : Loading
    }
}
