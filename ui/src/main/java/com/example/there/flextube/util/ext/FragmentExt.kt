package com.example.there.flextube.util.ext

import android.content.Context
import android.support.v4.app.Fragment
import com.example.there.flextube.main.MainActivity

val Fragment.accountName: String
    get() = activity!!.getPreferences(Context.MODE_PRIVATE).getString(MainActivity.PREF_ACCOUNT_NAME, null)
