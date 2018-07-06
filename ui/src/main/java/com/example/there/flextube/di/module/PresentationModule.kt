package com.example.there.flextube.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.di.vm.ViewModelKey
import com.example.there.presentation.subfeed.SubFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(SubFeedViewModel::class)
    abstract fun bindBrowseBufferoosViewModel(viewModel: SubFeedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}