package com.example.there.multifeeds.util.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
import com.example.there.multifeeds.R
import com.example.there.multifeeds.lifecycle.ConnectivityComponent

fun Activity.registerFragmentLifecycleCallbacks(
        callbacks: FragmentManager.FragmentLifecycleCallbacks,
        recursive: Boolean
) = (this as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
        callbacks,
        recursive
)

fun Activity.defaultConnectivityComponentSnackbarParams(
        parentView: View
): ConnectivityComponent.SnackbarParameters = ConnectivityComponent.SnackbarParameters(
        parentView = parentView,
        text = getString(R.string.no_internet_connection),
        actionBtnText = getString(R.string.settings),
        onActionBtnPressed = View.OnClickListener { goToSettings() }
)

fun Activity.goToSettings() = startActivity(Intent(Settings.ACTION_SETTINGS))

val Activity.isConnectedToInternet: Boolean
    get() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }