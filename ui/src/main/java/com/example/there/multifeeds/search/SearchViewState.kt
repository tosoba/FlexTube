package com.example.there.multifeeds.search

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.model.PlaylistItem

data class SearchViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val foundVideos: ObservableArrayList<PlaylistItem> = ObservableArrayList()
)