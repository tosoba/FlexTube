package com.example.there.flextube.di.module

import com.example.there.data.repo.store.base.IYoutubeRemote
import com.example.there.flextube.BuildConfig
import com.example.there.remote.YoutubeRemote
import com.example.there.remote.YoutubeService
import com.example.there.remote.YoutubeServiceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RemoteModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun youtubeService(): YoutubeService = YoutubeServiceFactory.makeYoutubeService(BuildConfig.DEBUG)
    }

    @Binds
    abstract fun youtubeRemote(youtubeRemote: YoutubeRemote): IYoutubeRemote
}