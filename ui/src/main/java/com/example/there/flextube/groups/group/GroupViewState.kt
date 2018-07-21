package com.example.there.flextube.groups.group

import android.databinding.ObservableArrayList
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription

data class GroupViewState(
        val subscriptions: ObservableArrayList<Subscription> = ObservableArrayList(),
        val videos: ObservableArrayList<PlaylistItem> = ObservableArrayList()
)