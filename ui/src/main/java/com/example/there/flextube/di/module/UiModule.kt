package com.example.there.flextube.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.di.vm.ViewModelKey
import com.example.there.flextube.home.HomeFragment
import com.example.there.flextube.home.HomeViewModel
import com.example.there.flextube.main.MainActivity
import com.example.there.flextube.subfeed.SubFeedFragment
import com.example.there.flextube.subfeed.SubFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UiModule {
    @Binds
    @IntoMap
    @ViewModelKey(SubFeedViewModel::class)
    abstract fun subFeedViewModel(viewModel: SubFeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun subFeedFragment(): SubFeedFragment

    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment
}