package com.example.there.multifeeds.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import com.example.there.multifeeds.util.ext.safelyDispose
import com.example.there.multifeeds.util.ext.setTextColor
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ConnectivityComponent(
        private val isDataLoaded: Boolean,
        private val reloadDataOnConnected: () -> Unit,
        private val snackbarParameters: SnackbarParameters
) : LifecycleObserver {

    class SnackbarParameters(
            val parentView: View,
            val text: String,
            val actionBtnText: String,
            val onActionBtnPressed: View.OnClickListener
    )

    private var internetDisposable: Disposable? = null
    private var connectionWasInterrupted = false
    private var lastConnectionStatus: Boolean? = null

    private var isSnackbarShowing = false
    private var snackbar: Snackbar? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    if (lastConnectionStatus != isConnectedToInternet) {
                        lastConnectionStatus = isConnectedToInternet
                        onConnectionStatusChanged(isConnectedToInternet)
                    }
                }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun clear() {
        snackbar?.dismiss()
        snackbar = null
        internetDisposable?.safelyDispose()
    }

    private fun onConnectionStatusChanged(isConnected: Boolean) {
        if (!isConnected) {
            connectionWasInterrupted = true
            if (!isSnackbarShowing) showNoConnectionSnackbar()
        } else {
            if (connectionWasInterrupted) {
                connectionWasInterrupted = false
                if (!isDataLoaded) reloadDataOnConnected()
            }

            isSnackbarShowing = false
            snackbar?.dismiss()
        }
    }

    private fun showNoConnectionSnackbar() {
        snackbar = Snackbar.make(snackbarParameters.parentView, snackbarParameters.text, Snackbar.LENGTH_LONG)
                .setAction(snackbarParameters.actionBtnText, snackbarParameters.onActionBtnPressed)
                .setCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        if (event == DISMISS_EVENT_SWIPE) showNoConnectionSnackbar()
                    }
                })
                .apply {
                    setTextColor(Color.RED)
                    duration = Snackbar.LENGTH_INDEFINITE
                    show()
                }
    }
}