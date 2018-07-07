package com.example.there.flextube.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.example.there.cache.YoutubeCache
import com.example.there.cache.db.FlexTubeDb
import com.example.there.data.repo.store.base.IYoutubeCache
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CacheModule {
    @Binds
    abstract fun youtubeCache(cache: YoutubeCache): IYoutubeCache

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideBufferoosDatabase(application: Application): FlexTubeDb = Room.databaseBuilder(
                application.applicationContext,
                FlexTubeDb::class.java, "flextube.db"
        ).build()
    }
}