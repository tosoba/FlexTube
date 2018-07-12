package com.example.there.flextube.home

import android.support.v7.widget.RecyclerView
import com.example.there.flextube.list.VideosAdapter

data class HomeView(
        val state: HomeViewState,
        val adapter: VideosAdapter,
        val itemDecoration: RecyclerView.ItemDecoration
)