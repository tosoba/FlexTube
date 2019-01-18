package com.example.there.multifeeds.util.ext

import android.preference.PreferenceManager
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.example.there.multifeeds.R
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.start.StartActivity

val Fragment.accountName: String
    get() = PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(StartActivity.PREF_ACCOUNT_NAME, null)

val Fragment.mainActivity: MainActivity?
    get() = activity as? MainActivity

val Fragment.mainToolbar: Toolbar?
    get() = mainActivity?.findViewById(R.id.main_toolbar)

fun Fragment.expandMainAppBar() = mainActivity?.findViewById<AppBarLayout>(R.id.main_app_bar_layout)?.setExpanded(true, true)
