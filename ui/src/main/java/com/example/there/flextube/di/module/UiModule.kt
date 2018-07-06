package com.example.there.flextube.di.module

import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.subfeed.SubFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun subFeedFragment(): SubFeedFragment
}