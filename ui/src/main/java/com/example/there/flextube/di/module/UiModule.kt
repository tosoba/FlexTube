package com.example.there.flextube.di.module

import com.example.there.flextube.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}