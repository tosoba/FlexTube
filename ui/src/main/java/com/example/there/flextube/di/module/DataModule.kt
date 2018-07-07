package com.example.there.flextube.di.module

import com.example.there.data.repo.MainRepository
import com.example.there.data.repo.store.base.IYoutubeDataStore
import com.example.there.data.repo.store.impl.YoutubeCachedDataStore
import com.example.there.data.repo.store.impl.YoutubeRemoteDataStore
import com.example.there.domain.repo.IMainRepository
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class DataModule {
    @Binds
    @Named(YoutubeRemoteDataStore.NAME)
    abstract fun remoteYoutubeDataStore(youtubeRemoteDataStore: YoutubeRemoteDataStore): IYoutubeDataStore

    @Binds
    @Named(YoutubeCachedDataStore.NAME)
    abstract fun cacheYoutubeDataStore(youtubeCachedDataStore: YoutubeCachedDataStore): IYoutubeDataStore

    @Binds
    abstract fun mainRepository(repository: MainRepository): IMainRepository
}