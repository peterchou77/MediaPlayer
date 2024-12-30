package com.pc.mediaplayer.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.pc.mediaplayer.model.Mp3FileData
import com.pc.mediaplayer.repository.Mp3Repository
import com.pc.mediaplayer.service.MediaServiceHandler
import com.pc.mediaplayer.service.MediaState
import com.pc.mediaplayer.service.PlayerEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MusicViewModel(
    val mp3Repository: Mp3Repository,
    val mediaServiceHandler: MediaServiceHandler,
) : ViewModel() {
    private val _currentTrack = mutableIntStateOf(0)
    val currentTrack: State<Int> = _currentTrack

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private val _duration = mutableLongStateOf(0)
    val duration: State<Long> = _duration

    private val mp3Files = mp3Repository.getMp3Files()

    private val _currentMp3FileData = mutableStateOf<Mp3FileData?>(null)
    val currentMp3FileData: State<Mp3FileData?> = _currentMp3FileData

    private val _progress = mutableFloatStateOf(0f)
    val progress: State<Float> = _progress

    private val _progressString = mutableStateOf("00:00")
    val progressString: State<String> = _progressString

    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()

            mediaServiceHandler.mediaState.collect { mediaState ->
                when (mediaState) {
                    is MediaState.Buffering -> calculateProgressValues(mediaState.progress)
                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.Playing -> _isPlaying.value = mediaState.isPlaying
                    is MediaState.Progress -> calculateProgressValues(mediaState.progress)
                    is MediaState.Ready -> {
                        _currentMp3FileData.value =
                            mp3Files[mediaServiceHandler.getCurrentIndex()]
                        _duration.longValue = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }

        }
    }

    private fun calculateProgressValues(currentProgress: Long) {
        _progress.floatValue =
            if (currentProgress > 0) (currentProgress.toFloat() / duration.value) else 0f
        _progressString.value = formatDuration(currentProgress)
    }

    fun onUIEvent(uiEvent: UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            UIEvent.Backward -> mediaServiceHandler.onPlayerEvent(PlayerEvent.Backward)
            UIEvent.Forward -> mediaServiceHandler.onPlayerEvent(PlayerEvent.Forward)
            UIEvent.Next -> {
                mediaServiceHandler.onPlayerEvent(PlayerEvent.Next)
                _currentTrack.intValue = mediaServiceHandler.getCurrentIndex()
                _currentMp3FileData.value = mp3Files[_currentTrack.intValue]
            }

            UIEvent.Previous -> {
                mediaServiceHandler.onPlayerEvent(PlayerEvent.Previous)
                _currentTrack.intValue = mediaServiceHandler.getCurrentIndex()
                _currentMp3FileData.value = mp3Files[_currentTrack.intValue]
            }

            UIEvent.PlayPause -> mediaServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
            is UIEvent.UpdateProgress -> {
                mediaServiceHandler.onPlayerEvent(
                    PlayerEvent.UpdateProgress(
                        uiEvent.newProgress
                    )
                )
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(duration: Long): String {
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadData() {
        val mediaItemList = mutableListOf<MediaItem>()
        mp3Files.forEach {
            mediaItemList.add(
                MediaItem.Builder()
                    .setUri(Uri.parse("asset:///${it.fileName}")).build()
            )
        }
        mediaServiceHandler.addMediaItemList(mediaItemList)
    }

    fun setTrack(index: Int) {
        _currentTrack.intValue = index
        _currentMp3FileData.value = mp3Files[_currentTrack.intValue]
        loadData()
        mediaServiceHandler.setCurrentTrack(index)
    }

}

sealed class UIEvent {
    data object PlayPause : UIEvent()
    data object Backward : UIEvent()
    data object Forward : UIEvent()
    data object Next : UIEvent()
    data object Previous : UIEvent()
    data class UpdateProgress(val newProgress: Float) : UIEvent()
}

sealed class UIState {
    data object Initial : UIState()
    data object Ready : UIState()
}