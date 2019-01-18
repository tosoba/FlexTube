package com.example.there.multifeeds.util.ext

import io.reactivex.disposables.Disposable

fun Disposable.safelyDispose() {
    if (!isDisposed) dispose()
}