package com.example.there.multifeeds.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposablesComponent : LifecycleObserver {
    private val disposables = CompositeDisposable()

    fun add(disposable: Disposable) = disposables.add(disposable)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() = disposables.clear()
}