package com.example.there.flextube.subfeed

import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.VideosAdapter

data class SubFeedView(
        val state: SubFeedViewState,
        val subscriptionsAdapter: SubFeedSubscriptionsAdapter,
        val videosAdapter: VideosAdapter,
        val videosItemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener
)