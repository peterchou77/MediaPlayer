package com.pc.mediaplayer.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pc.mediaplayer.R
import com.pc.mediaplayer.viewmodel.Mp3ViewModel
import com.pc.mediaplayer.viewmodel.MusicViewModel
import com.pc.mediaplayer.viewmodel.UIEvent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    navController: NavController,
    mp3ViewModel: Mp3ViewModel,
    musicViewModel:
    MusicViewModel = koinViewModel(),
) {
    val onUiEvent = musicViewModel::onUIEvent
    val currentTrack = musicViewModel.currentTrack
    val isPlaying by musicViewModel.isPlaying

    val currentMp3FileData = musicViewModel.currentMp3FileData
    val duration by musicViewModel.duration
    val itemIndex = mp3ViewModel.selectedMp3Item.value?.index ?: 0

    val newProgressValue = remember { mutableFloatStateOf(0f) }
    val useNewProgressValue = remember { mutableStateOf(false) }



    LaunchedEffect(currentTrack) {
        musicViewModel.setTrack(itemIndex)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        currentMp3FileData.value?.fileName ?: "",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {

            if (currentMp3FileData.value?.albumArt != null) {
                currentMp3FileData.value?.albumArt?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.music_list_no_image),
                    modifier = Modifier.size(64.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Slider(
                    value = if (useNewProgressValue.value) newProgressValue.floatValue else musicViewModel.progress.value,
                    onValueChange = { newValue ->
                        useNewProgressValue.value = true
                        newProgressValue.floatValue = newValue
                        onUiEvent(UIEvent.UpdateProgress(newProgress = newValue))
                    },
                    onValueChangeFinished = {
                        useNewProgressValue.value = false
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = musicViewModel.progressString.value,
                        style = typography.bodySmall,
                        color = Color.White
                    )
                    Text(
                        text = formatTime(duration),
                        style = typography.bodySmall,
                        color = Color.White
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        onUiEvent(UIEvent.Previous)
                    }) {
                        Icon(
                            painter = painterResource(id = R.mipmap.ic_previous),
                            contentDescription = "Previous",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    IconButton(onClick = {
                        onUiEvent(UIEvent.PlayPause)
                    }) {
                        Icon(
                            painter = if (isPlaying) painterResource(id = R.mipmap.ic_pause) else painterResource(
                                id = R.mipmap.ic_play
                            ),
                            contentDescription = "Play/Pause",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    IconButton(onClick = {
                        onUiEvent(UIEvent.Next)
                    }) {
                        Icon(
                            painter = painterResource(id = R.mipmap.ic_next),
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}