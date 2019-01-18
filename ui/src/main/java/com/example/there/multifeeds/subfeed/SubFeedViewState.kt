package com.example.there.multifeeds.subfeed

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.model.PlaylistItem
import com.example.there.multifeeds.model.UiSubscription
import com.example.there.multifeeds.util.view.ObservableSortedList

data class SubFeedViewState(
        val subscriptions: ObservableArrayList<UiSubscription> = ObservableArrayList(),
        val videos: ObservableSortedList<PlaylistItem> = ObservableSortedList(PlaylistItem::class.java, object : ObservableSortedList.Callback<PlaylistItem> {
            override fun compare(o1: PlaylistItem, o2: PlaylistItem): Int = o2.publishedAt.compareTo(o1.publishedAt)

            override fun areItemsTheSame(item1: PlaylistItem, item2: PlaylistItem): Boolean = item1.videoId == item2.videoId

            override fun areContentsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem): Boolean = newItem.videoId == oldItem.videoId
        }),
        val noSubscriptions: ObservableField<Boolean> = ObservableField(false)
)