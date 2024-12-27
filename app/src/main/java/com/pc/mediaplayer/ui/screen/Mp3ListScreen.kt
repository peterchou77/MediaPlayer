package com.pc.mediaplayer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pc.mediaplayer.R
import com.pc.mediaplayer.model.Mp3FileData
import com.pc.mediaplayer.ui.Routes
import com.pc.mediaplayer.viewmodel.Mp3ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mp3ListScreen(navController: NavController, mp3ViewModel: Mp3ViewModel) {
    val mp3Files by mp3ViewModel.mp3Files.collectAsState()

    LaunchedEffect(Unit) {
        mp3ViewModel.loadMp3Files()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.music_list_title),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF50151C)
                ),
                modifier = Modifier
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentPadding = paddingValues
        ) {
            items(mp3Files) { mp3 ->
                Mp3Item(navController, mp3, mp3ViewModel)
            }
        }

    }
}

@Composable
fun Mp3Item(navController: NavController, mp3: Mp3FileData, mp3ViewModel: Mp3ViewModel) {
    Box(modifier = Modifier.clickable {
        mp3ViewModel.selectItem(mp3)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mp3.albumArt != null) {
                Image(
                    bitmap = mp3.albumArt.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.music_list_no_image),
                    modifier = Modifier.size(64.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = mp3.fileName)
        }
    }
}
