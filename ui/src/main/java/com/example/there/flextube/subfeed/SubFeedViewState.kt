package com.example.there.flextube.subfeed

import android.databinding.ObservableArrayList
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription

data class SubFeedViewState(
        val subscriptions: ObservableArrayList<Subscription> = ObservableArrayList(),
        val videos: ObservableArrayList<PlaylistItem> = ObservableArrayList()
)