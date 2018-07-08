package com.example.there.flextube.di

import android.app.Application
import com.example.there.flextube.FlexTubeApp
import com.example.there.flextube.di.module.*
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
    DomainModule::class,
    UiModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: FlexTubeApp)

}