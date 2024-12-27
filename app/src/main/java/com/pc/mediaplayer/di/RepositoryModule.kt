package com.pc.mediaplayer.di

import com.pc.mediaplayer.repository.Mp3Repository
import com.pc.mediaplayer.repository.Mp3RepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<Mp3Repository> { Mp3RepositoryImpl(get()) }
}