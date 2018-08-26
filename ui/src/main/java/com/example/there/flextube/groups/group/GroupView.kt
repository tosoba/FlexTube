package com.example.there.flextube.groups.group

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.flextube.list.SubscriptionsVideosAdapter

data class GroupView(
        val videosAdapter: SubscriptionsVideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener,
        val onAddMoreSubsClickListener: View.OnClickListener,
        val onDeleteGroupClickListener: View.OnClickListener
)