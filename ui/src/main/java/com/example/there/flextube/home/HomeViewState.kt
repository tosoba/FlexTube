package com.example.there.flextube.home

import android.databinding.ObservableArrayList
import com.example.there.domain.model.PlaylistItem

data class HomeViewState(
        val homeItems: ObservableArrayList<PlaylistItem> = ObservableArrayList()
)