package com.example.there.flextube.subfeed

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.SubscriptionsVideosAdapter

data class SubFeedView(
        val state: SubFeedViewState,
        val videosAdapter: SubscriptionsVideosAdapter,
        val videosItemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener,
        val onSubFeedRefreshListener: SwipeRefreshLayout.OnRefreshListener
)