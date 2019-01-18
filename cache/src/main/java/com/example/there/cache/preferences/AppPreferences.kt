package com.example.there.cache.preferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(context: Context) {
    var accountName: String? by SharedPreference(context, PreferencesEntry.AccountName)
}