package com.example.there.multifeeds.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.multifeeds.addgroup.AddGroupActivity
import com.example.there.multifeeds.addgroup.AddGroupViewModel
import com.example.there.multifeeds.di.vm.ViewModelFactory
import com.example.there.multifeeds.di.vm.ViewModelKey
import com.example.there.multifeeds.groups.group.GroupFragment
import com.example.there.multifeeds.groups.group.GroupViewModel
import com.example.there.multifeeds.groups.list.GroupsListFragment
import com.example.there.multifeeds.groups.list.GroupsListViewModel
import com.example.there.multifeeds.home.HomeFragment
import com.example.there.multifeeds.home.HomeViewModel
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.main.MainViewModel
import com.example.there.multifeeds.search.SearchFragment
import com.example.there.multifeeds.search.SearchViewModel
import com.example.there.multifeeds.subfeed.SubFeedFragment
import com.example.there.multifeeds.subfeed.SubFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UiModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

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
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel

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

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment
}