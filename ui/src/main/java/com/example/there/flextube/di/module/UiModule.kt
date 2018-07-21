package com.example.there.flextube.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.flextube.addgroup.AddGroupActivity
import com.example.there.flextube.addgroup.AddGroupViewModel
import com.example.there.flextube.di.vm.ViewModelFactory
import com.example.there.flextube.di.vm.ViewModelKey
import com.example.there.flextube.groups.group.GroupFragment
import com.example.there.flextube.groups.group.GroupViewModel
import com.example.there.flextube.groups.list.GroupsListFragment
import com.example.there.flextube.groups.list.GroupsListViewModel
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
    @IntoMap
    @ViewModelKey(GroupsListViewModel::class)
    abstract fun groupsListViewModel(viewModel: GroupsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupViewModel::class)
    abstract fun groupViewModel(viewModel: GroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddGroupViewModel::class)
    abstract fun addGroupViewModel(viewModel: AddGroupViewModel): ViewModel

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun addGroupActivity(): AddGroupActivity

    @ContributesAndroidInjector
    abstract fun subFeedFragment(): SubFeedFragment

    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun groupsListFragment(): GroupsListFragment

    @ContributesAndroidInjector
    abstract fun groupFragment(): GroupFragment
}