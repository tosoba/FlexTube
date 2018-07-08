package com.example.there.flextube.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import org.greenrobot.eventbus.EventBus

class EventBusComponent(private val fragment: Fragment): LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun register() {
        EventBus.getDefault().register(fragment)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() {
        EventBus.getDefault().unregister(fragment)
    }
}