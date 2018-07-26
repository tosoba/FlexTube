package com.example.there.flextube.util.ext

import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import com.example.there.flextube.start.StartActivity

val Fragment.accountName: String
    get() = PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(StartActivity.PREF_ACCOUNT_NAME, null)

