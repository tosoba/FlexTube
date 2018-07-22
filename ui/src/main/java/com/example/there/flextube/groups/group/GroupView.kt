package com.example.there.flextube.groups.group

import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.SortedVideosAdapter
import com.example.there.flextube.subfeed.SubFeedSubscriptionsAdapter

data class GroupView(
        val subscriptionsAdapter: SubFeedSubscriptionsAdapter,
        val videosAdapter: SortedVideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener
)