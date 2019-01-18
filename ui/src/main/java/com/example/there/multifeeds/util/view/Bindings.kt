package com.example.there.multifeeds.util.view

import android.databinding.BindingAdapter
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.there.multifeeds.R
import com.sothree.slidinguppanel.SlidingUpPanelLayout

@BindingAdapter("onNavigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener) {
    bottomNavigationView.setOnNavigationItemSelectedListener(listener)
}

@BindingAdapter("videoThumbnailUrl")
fun bindVideoThumbnailUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(view)
    }
}

@BindingAdapter("subscriptionThumbnailUrl")
fun bindSubscriptionThumbnailUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(view)
    }
}

@BindingAdapter("itemDecoration")
fun bindItemDecoration(recycler: RecyclerView, decoration: RecyclerView.ItemDecoration) {
    recycler.addItemDecoration(decoration)
}

@BindingAdapter("onScrollListener")
fun bindOnScrollListener(recycler: RecyclerView, listener: RecyclerView.OnScrollListener) {
    recycler.addOnScrollListener(listener)
}

@BindingAdapter("onPageChangeListener")
fun bindOnPageChangeListener(viewPager: ViewPager, listener: ViewPager.OnPageChangeListener) {
    viewPager.addOnPageChangeListener(listener)
}

@BindingAdapter("fragmentStatePagerAdapter")
fun bindFragmentStatePagerAdapter(viewPager: ViewPager, adapter: FragmentStatePagerAdapter) {
    viewPager.adapter = adapter
}

@BindingAdapter("offScreenPageLimit")
fun bindOffScreenPageLimit(viewPager: ViewPager, limit: Int) {
    viewPager.offscreenPageLimit = limit
}

@BindingAdapter("srcId")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("onQueryTextListener")
fun setOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
    searchView.setOnQueryTextListener(listener)
}

@BindingAdapter("panelSlideListener")
fun bindPanelSlideListener(slidingUpPanelLayout: SlidingUpPanelLayout, listener: SlidingUpPanelLayout.PanelSlideListener) {
    slidingUpPanelLayout.addPanelSlideListener(listener)
}

@BindingAdapter("fadeOnClickListener")
fun setFadeOnClickListener(slidingUpPanelLayout: SlidingUpPanelLayout, listener: View.OnClickListener) {
    slidingUpPanelLayout.setFadeOnClickListener(listener)
}

@BindingAdapter("initialSlideState")
fun setInitialPanelSlideState(slidingUpPanelLayout: SlidingUpPanelLayout, state: SlidingUpPanelLayout.PanelState) {
    slidingUpPanelLayout.panelState = state
}

@BindingAdapter("onRefreshListener")
fun setOnRefreshListener(swipeRefreshLayout: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
    swipeRefreshLayout.setOnRefreshListener(listener)
}