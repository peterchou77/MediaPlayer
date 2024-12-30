package com.pc.mediaplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pc.mediaplayer.service.MediaService
import com.pc.mediaplayer.ui.Routes
import com.pc.mediaplayer.ui.screen.Mp3ListScreen
import com.pc.mediaplayer.ui.screen.MusicScreen
import com.pc.mediaplayer.ui.theme.MediaPlayerTheme
import com.pc.mediaplayer.viewmodel.Mp3ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mp3ViewModel: Mp3ViewModel by viewModel()
    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()
        enableEdgeToEdge()
        setContent {
            MediaPlayerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.MP3_LIST
                ) {
                    composable(Routes.MP3_LIST) {
                        Mp3ListScreen(navController = navController, mp3ViewModel = mp3ViewModel)
                    }
                    composable(Routes.MP3_DETAIL) {
                        MusicScreen(navController, mp3ViewModel)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MediaService::class.java))
        isServiceRunning = false
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(this, MediaService::class.java)
            startForegroundService(intent)
            isServiceRunning = true
        }
    }

}
