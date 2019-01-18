package com.example.there.multifeeds.home

import android.support.v7.widget.RecyclerView
import com.example.there.multifeeds.list.CategoryVideosAdapter

class HomeView(
        val state: HomeViewState,
        val videosAdapter: CategoryVideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onScrollListener: RecyclerView.OnScrollListener
)