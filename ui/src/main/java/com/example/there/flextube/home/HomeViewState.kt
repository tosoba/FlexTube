package com.example.there.flextube.home

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.model.PlaylistItem

data class HomeViewState(
        val homeItems: ObservableArrayList<PlaylistItem> = ObservableArrayList(),
        val isLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)