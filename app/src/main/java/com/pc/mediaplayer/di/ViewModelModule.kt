package com.pc.mediaplayer.di

import android.annotation.SuppressLint
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.pc.mediaplayer.service.MediaServiceHandler
import com.pc.mediaplayer.service.notification.MediaNotificationManager
import com.pc.mediaplayer.viewmodel.Mp3ViewModel
import com.pc.mediaplayer.viewmodel.MusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val viewModelModule = module {
    single { ExoPlayer.Builder(get()).build() }
    single { MediaSession.Builder(get(), get<ExoPlayer>()).build() }
    single { MediaNotificationManager(get(), get()) }
    single { MediaServiceHandler(get()) }
    viewModel { Mp3ViewModel(repository = get()) }
    viewModel { MusicViewModel(mp3Repository = get(), mediaServiceHandler = get()) }
}