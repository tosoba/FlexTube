package com.example.there.multifeeds.list

import android.databinding.ObservableField

data class LoadingItemViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)