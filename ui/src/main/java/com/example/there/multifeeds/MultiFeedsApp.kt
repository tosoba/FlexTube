package com.example.there.multifeeds

import android.app.Activity
import android.app.Application
import com.example.there.multifeeds.di.AppInjector
import com.example.there.multifeeds.util.di.HasActivityDispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MultiFeedsApp : Application(), HasActivityDispatchingAndroidInjector {

    @Inject
    override lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }
}