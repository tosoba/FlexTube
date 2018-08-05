package com.example.there.flextube.base.vm

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

    fun clearDisposables() {
        disposables.clear()
    }
}