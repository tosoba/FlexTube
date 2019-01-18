package com.example.there.cache.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreference<T>(
        context: Context,
        private val entry: PreferencesEntry<T>
) : ReadWriteProperty<Any?, T> {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        if (!preferences.contains(entry.key) && entry.options.initializeWithDefault && entry.defaultValue != null)
            put(entry.defaultValue)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = with(preferences) {
        val (key, defaultValue) = entry

        val res: Any? = when (defaultValue) {
            is Long -> getLong(key, defaultValue)
            is String -> getString(key, defaultValue)
            is String? -> getString(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            else -> throw IllegalArgumentException(INVALID_TYPE_EXCEPTION_MSG)
        }

        @Suppress("UNCHECKED_CAST")
        val prefValue = res as T

        if (entry.options.throwIfUnset && prefValue == defaultValue)
            throw IllegalStateException(PREFERENCE_VALUE_NOT_SET_EXCEPTION_MSG)

        return@with prefValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (value != null) {
            if (!preferences.contains(entry.key) || (preferences.contains(entry.key) && entry.options.mutableAfterOnceSet))
                put(value)
        } else {
            remove()
        }
    }

    private fun put(value: T) = with(preferences.edit()) {
        val (key, _) = entry
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is String? -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> throw IllegalArgumentException(INVALID_TYPE_EXCEPTION_MSG)
        }.apply()
    }

    private fun remove() = with(preferences.edit()) { remove(entry.key) }.apply()

    companion object {
        private const val INVALID_TYPE_EXCEPTION_MSG = "This type cannot be saved into preferences."
        private const val PREFERENCE_VALUE_NOT_SET_EXCEPTION_MSG = "Preference value not set yet."
    }
}