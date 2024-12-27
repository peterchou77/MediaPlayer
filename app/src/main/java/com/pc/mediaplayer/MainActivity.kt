package com.pc.mediaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pc.mediaplayer.ui.Routes
import com.pc.mediaplayer.ui.screen.Mp3ListScreen
import com.pc.mediaplayer.ui.theme.MediaPlayerTheme
import com.pc.mediaplayer.viewmodel.Mp3ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mp3ViewModel: Mp3ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                }
            }
        }
    }
}
