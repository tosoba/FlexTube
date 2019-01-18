package com.example.there.multifeeds.util.ext

import android.support.v7.widget.RecyclerView
import com.example.there.multifeeds.base.list.adapter.TracksInitialScroll

fun RecyclerView.addOnInitialUserScrollListener() {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dx != 0 || dy != 0) {
                (recyclerView?.adapter as? TracksInitialScroll)?.userHasScrolled = true
                recyclerView?.removeOnScrollListener(this)
            }
        }
    })
}