package com.example.there.flextube.groups.group

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.flextube.list.VideosAdapter
import com.example.there.flextube.subfeed.SubFeedSubscriptionsAdapter

data class GroupView(
        val subscriptionsAdapter: SubFeedSubscriptionsAdapter,
        val videosAdapter: VideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener,
        val onAddMoreSubsClickListener: View.OnClickListener,
        val onDeleteGroupClickListener: View.OnClickListener
)