package com.example.there.flextube.list

import android.databinding.ObservableField

data class LoadingItemViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)