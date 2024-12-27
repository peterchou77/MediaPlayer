package com.pc.mediaplayer

import android.app.Application
import com.pc.mediaplayer.di.repositoryModule
import com.pc.mediaplayer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication  : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(repositoryModule)
            modules(viewModelModule)
        }
    }
}