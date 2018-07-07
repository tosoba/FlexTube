package com.example.there.flextube.di.module

import com.example.there.data.repo.MainRepository
import com.example.there.data.repo.store.base.IYoutubeDataStore
import com.example.there.data.repo.store.impl.RemoteYoutubeDataStore
import com.example.there.domain.repo.IMainRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun remoteYoutubeDataStore(remoteYoutubeDataStore: RemoteYoutubeDataStore): IYoutubeDataStore

    @Binds
    abstract fun mainRepository(repository: MainRepository): IMainRepository
}