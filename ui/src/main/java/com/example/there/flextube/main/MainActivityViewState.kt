package com.example.there.flextube.main

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.model.PlaylistItem

data class MainActivityViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val relatedVideos: ObservableArrayList<PlaylistItem> = ObservableArrayList()
)