package com.pc.mediaplayer.di

import android.annotation.SuppressLint
import com.pc.mediaplayer.viewmodel.Mp3ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val viewModelModule = module {
    viewModel { Mp3ViewModel(repository = get()) }
}