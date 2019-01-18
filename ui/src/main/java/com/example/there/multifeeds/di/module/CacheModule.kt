package com.example.there.multifeeds.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.example.there.cache.YoutubeCache
import com.example.there.cache.db.MultiFeedsDb
import com.example.there.data.repo.store.IYoutubeCache
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
        fun multiFeedsDB(application: Application): MultiFeedsDb = Room.databaseBuilder(
                application.applicationContext,
                MultiFeedsDb::class.java, "multifeeds.db"
        ).build()
    }
}