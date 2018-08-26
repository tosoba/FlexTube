package com.example.there.flextube.groups.group

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.flextube.list.SubscriptionsAdapter
import com.example.there.flextube.list.VideosAdapter

data class GroupView(
        val subscriptionsAdapter: SubscriptionsAdapter,
        val videosAdapter: VideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener,
        val onAddMoreSubsClickListener: View.OnClickListener,
        val onDeleteGroupClickListener: View.OnClickListener
)