package com.example.there.flextube.home

import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.CategoryVideosAdapter

data class HomeView(
        val state: HomeViewState,
        val videosAdapter: CategoryVideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onScrollListener: RecyclerView.OnScrollListener
)