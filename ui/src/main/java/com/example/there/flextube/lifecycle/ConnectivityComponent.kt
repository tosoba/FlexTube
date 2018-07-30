package com.example.there.flextube.lifecycle

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ConnectivityComponent(
        private val activity: Activity,
        private val isDataLoaded: Boolean,
        private val reloadData: () -> Unit,
        private val snackBarParentId: Int
) : LifecycleObserver {

    private var internetDisposable: Disposable? = null
    private var connectionInterrupted = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    if (lastConnectionStatus != isConnectedToInternet) {
                        lastConnectionStatus = isConnectedToInternet
                        handleConnectionStatus(isConnectedToInternet)
                    }
                }
    }

    private var lastConnectionStatus: Boolean? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun clear() {
        snackbar?.dismiss()
        snackbar = null
        safelyDispose(internetDisposable)
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun handleConnectionStatus(isConnectedToInternet: Boolean) {
        if (!isConnectedToInternet) {
            connectionInterrupted = true
            if (!isSnackbarShowing) {
                showNoConnectionDialog()
            }
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false
                if (!isDataLoaded) {
                    reloadData()
                }
            }

            isSnackbarShowing = false
            snackbar?.dismiss()
        }
    }

    private var isSnackbarShowing = false

    private var snackbar: Snackbar? = null

    private fun showNoConnectionDialog() {
        snackbar = Snackbar
                .make(activity.findViewById(snackBarParentId), "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS") {
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                    activity.startActivity(settingsIntent)
                }
                .setCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        if (event == DISMISS_EVENT_SWIPE) {
                            showNoConnectionDialog()
                        }
                    }
                })

        val textView = snackbar?.view?.findViewById(android.support.design.R.id.snackbar_text) as? TextView
        textView?.setTextColor(Color.RED)
        snackbar?.duration = Snackbar.LENGTH_INDEFINITE
        snackbar?.show()
    }
}