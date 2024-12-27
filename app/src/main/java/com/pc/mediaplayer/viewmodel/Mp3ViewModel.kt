package com.pc.mediaplayer.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pc.mediaplayer.model.Mp3FileData
import com.pc.mediaplayer.repository.Mp3Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Mp3ViewModel(private val repository: Mp3Repository) : ViewModel() {

    private val _mp3Files = MutableStateFlow<List<Mp3FileData>>(emptyList())
    val mp3Files: StateFlow<List<Mp3FileData>> = _mp3Files

    private val _selectedMp3Item = mutableStateOf<Mp3FileData?>(null)
    val selectedMp3Item: State<Mp3FileData?> = _selectedMp3Item

    fun selectItem(item: Mp3FileData) {
        _selectedMp3Item.value = item
    }

    fun loadMp3Files() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = repository.getMp3Files()
            _mp3Files.value = files
        }
    }
}