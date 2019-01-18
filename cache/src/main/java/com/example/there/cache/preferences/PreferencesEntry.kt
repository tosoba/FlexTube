package com.example.there.cache.preferences

sealed class PreferencesEntry<T>(
        val key: String,
        val defaultValue: T,
        val options: Options
) {
    operator fun component1(): String = key
    operator fun component2(): T = defaultValue

    object AccountName : PreferencesEntry<String?>("PREF_ACCOUNT_NAME", null, Options(
            initializeWithDefault = false,
            mutableAfterOnceSet = true,
            throwIfUnset = false
    ))

    class Options(
            val initializeWithDefault: Boolean,
            val mutableAfterOnceSet: Boolean,
            val throwIfUnset: Boolean
    )
}