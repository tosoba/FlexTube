package com.example.there.multifeeds.start

import android.databinding.ObservableField

data class StartViewState(
        val splashOnly: ObservableField<Boolean> = ObservableField(true),
        val authInProgress: ObservableField<Boolean> = ObservableField(false)
)