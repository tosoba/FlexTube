package com.example.there.multifeeds.search

import android.support.v7.widget.RecyclerView
import com.example.there.multifeeds.list.VideosAdapter

class SearchView(
        val state: SearchViewState,
        val foundVideosAdapter: VideosAdapter,
        val onFoundVideosScroll: RecyclerView.OnScrollListener,
        val itemDecoration: RecyclerView.ItemDecoration
)