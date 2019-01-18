package com.example.there.multifeeds.home

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.data.repo.store.IYoutubeCache
import com.example.there.domain.model.PlaylistItem
import com.example.there.multifeeds.model.UiVideoCategory

data class HomeViewState(
        val homeItems: ObservableArrayList<PlaylistItem> = ObservableArrayList(),
        val videoCategories: ObservableArrayList<UiVideoCategory> = ObservableArrayList(),
        val isLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        var currentVideoCategoryId: String = IYoutubeCache.CATEGORY_GENERAL
)