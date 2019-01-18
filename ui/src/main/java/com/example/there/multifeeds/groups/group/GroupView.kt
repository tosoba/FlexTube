package com.example.there.multifeeds.groups.group

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.multifeeds.list.SubscriptionsVideosAdapter

class GroupView(
        val videosAdapter: SubscriptionsVideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onVideosScrollListener: RecyclerView.OnScrollListener,
        val onAddMoreSubsClickListener: View.OnClickListener,
        val onDeleteGroupClickListener: View.OnClickListener
)