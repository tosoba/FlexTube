package com.example.there.flextube.home

import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.VideoCategoriesAdapter
import com.example.there.flextube.list.VideosAdapter

data class HomeView(
        val state: HomeViewState,
        val videosAdapter: VideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onScrollListener: RecyclerView.OnScrollListener,
        val videoCategoriesAdapter: VideoCategoriesAdapter
)