package com.example.there.flextube.util.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.there.flextube.base.list.adapter.BaseBindingLoadingAdapter


abstract class EndlessRecyclerOnScrollListener(private val visibleThreshold: Int = 5) : RecyclerView.OnScrollListener() {
    /**
     * The total number of items in the dataset after the last load
     */
    var mPreviousTotal = 0
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var mLoading = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView?.let {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerView.layoutManager.itemCount
            val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (totalItemCount == 0 || (recyclerView.adapter is BaseBindingLoadingAdapter<*, *> && totalItemCount == 1))
                return@let

            if (mLoading) {
                if (totalItemCount > mPreviousTotal) {
                    mLoading = false
                    mPreviousTotal = totalItemCount
                }
            }

            if (!mLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                onLoadMore()
                mLoading = true
            }
        }
    }

    abstract fun onLoadMore()
}