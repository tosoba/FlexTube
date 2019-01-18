package com.example.there.multifeeds.di

import android.app.Application
import com.example.there.multifeeds.MultiFeedsApp
import com.example.there.multifeeds.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AndroidSupportInjectionModule::class,
    CacheModule::class,
    RemoteModule::class,
    DataModule::class,
    UiModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MultiFeedsApp)

}